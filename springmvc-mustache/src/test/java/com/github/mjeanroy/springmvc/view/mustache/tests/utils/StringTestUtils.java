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

package com.github.mjeanroy.springmvc.view.mustache.tests.utils;

import java.util.Collection;

/**
 * Static String Utilities, to use in unit test only.
 */
public final class StringTestUtils {

	/**
	 * The OS dependant line separator.
	 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	// Ensure non instantiation.
	private StringTestUtils() {
	}

	/**
	 * Join given lines to a single string using the line separator character as
	 * join character.
	 *
	 * @param lines Lines to join.
	 * @return The entire text.
	 */
	public static String joinLines(Collection<String> lines) {
		StringBuilder sb = new StringBuilder();
		boolean firstLine = true;
		for (String line : lines) {
			if (!firstLine) {
				sb.append(LINE_SEPARATOR);
			}

			sb.append(line);
			firstLine = false;
		}

		return sb.toString();
	}
}
