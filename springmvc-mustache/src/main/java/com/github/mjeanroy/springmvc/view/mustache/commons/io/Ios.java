/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2020 Mickael Jeanroy
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

package com.github.mjeanroy.springmvc.view.mustache.commons.io;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Common static IO Utilities.
 */
public final class Ios {

	/**
	 * Buffer size to read files.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Line separator, system dependent.
	 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private Ios() {
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
		try (BufferedReader buffer = new BufferedReader(reader, BUFFER_SIZE)) {
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
	}
}
