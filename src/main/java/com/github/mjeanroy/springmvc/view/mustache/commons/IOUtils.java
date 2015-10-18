/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.mjeanroy.springmvc.view.mustache.commons;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

/**
 * Common static IO Utilities.
 */
public final class IOUtils {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

	/**
	 * Buffer size to read files.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Line separator, system dependent.
	 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private IOUtils() {
	}

	/**
	 * Read input reader: extract content and return it as
	 * a String.
	 *
	 * @param reader Reader input.
	 * @return String content.
	 * @throws MustacheIOException If an IO exception occurs during reading operation.
	 */
	public static String read(final Reader reader) {
		final BufferedReader buffer = new BufferedReader(reader, BUFFER_SIZE);

		try {
			StringBuilder sb = new StringBuilder();
			String line;
			boolean started = false;

			// Read line by line
			while ((line = buffer.readLine()) != null) {
				if (started) {
					sb.append(LINE_SEPARATOR);
				}

				sb.append(line);
				started = true;
			}

			return sb.toString();
		}
		catch (IOException ex) {
			throw new MustacheIOException(ex);
		}
		finally {
			closeQuietly(buffer);
		}
	}

	/**
	 * Return an input stream from given name.
	 * Name is searched into the classpath.
	 * Name can contains ant style pattern.
	 *
	 * @param name Name.
	 * @return Input Stream.
	 */
	public static InputStream getStream(String name) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(IOUtils.class.getClassLoader());
		InputStream stream = getInputStreamWithResolver(resolver, name);

		// Not Found
		if (stream == null) {
			throw new MustacheIOException("Unable to locate {}" + name);
		}

		return stream;
	}

	/**
	 * Return an input stream from given names.
	 * First result returning a valid stream is returned.
	 * Names are searched into the classpath.
	 * Names can contains ant style pattern.
	 *
	 * @param names Names collection.
	 * @return Input Stream.
	 * @throws MustacheIOException If everything fail.
	 */
	public static InputStream getFirstAvailableStream(Collection<String> names) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(IOUtils.class.getClassLoader());
		for (String name : names) {
			try {
				InputStream stream = getInputStreamWithResolver(resolver, name);
				if (stream != null) {
					return stream;
				}
			}
			catch (MustacheIOException ex) {
				// Go to the next test
			}
		}

		throw new MustacheIOException("Unable to locate one of: " + names);
	}

	private static InputStream getInputStreamWithResolver(ResourcePatternResolver resolver, String name) {
		try {
			Resource[] resources = resolver.getResources(name);

			// Not Found
			if (resources.length == 0) {
				log.debug("Found zero results with pattern: {}", name);
				return null;
			}

			log.debug("Found {} results with pattern: {}", resources.length, name);

			if (log.isTraceEnabled()) {
				for (Resource resource : resources) {
					log.trace("  -> Found: {}", resource.getFile().getCanonicalPath());
				}
			}

			return resources[0].getInputStream();
		}
		catch (IOException ex) {
			throw new MustacheIOException("I/O Error with " + name, ex);
		}
	}

	/**
	 * Close a {@link java.io.Closeable} object and do not throws a Checked Exception
	 * if an exception occurs.
	 *
	 * @param stream Closeable stream.
	 */
	private static void closeQuietly(Closeable stream) {
		try {
			stream.close();
		}
		catch (IOException ex) {
			// Just log.
			log.debug(ex.getMessage(), ex);
		}
	}
}
