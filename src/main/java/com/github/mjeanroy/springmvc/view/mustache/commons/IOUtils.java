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

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * Common static IO Utilities.
 */
public final class IOUtils {

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
		final int bufferSize = 1024;
		try {
			char[] cbuf = new char[bufferSize];
			StringBuilder sb = new StringBuilder(bufferSize);
			int len;
			while ((len = reader.read(cbuf, 0, bufferSize)) != -1) {
				sb.append(cbuf, 0, len);
			}
			return sb.toString();
		}
		catch (IOException ex) {
			throw new MustacheIOException(ex);
		}
		finally {
			closeQuietly(reader);
		}
	}

	/**
	 * Close a {@link java.io.Closeable} object and do not throws a Checked Exception
	 * if an exception occurs.
	 *
	 * @param stream Closeable stream.
	 */
	public static void closeQuietly(Closeable stream) {
		try {
			stream.close();
		}
		catch (IOException ex) {
			// Omitted.
		}
	}
}
