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

/**
 * Commons static pre-conditions utilities.
 */
public final class PreConditions {

	private PreConditions() {
	}

	/**
	 * Check that a given object is not null.
	 * If object is null, a {@link java.lang.NullPointerException} is thrown
	 * with given message.
	 * If object is not null, it is automatically returns.
	 *
	 * @param object  Object to check.
	 * @param message Exception message.
	 * @param <T>     Object Generic Type.
	 * @return Non null object.
	 * @throws java.lang.NullPointerException if object is null.
	 */
	public static <T> T notNull(T object, String message) {
		if (object == null) {
			throw new NullPointerException(message);
		}
		return object;
	}

	/**
	 * Check that a given string value is not blank (i.e. not null,
	 * not empty and does not contain only whitespaces).
	 * If value is blank, an {@link java.lang.IllegalArgumentException} is
	 * thrown.
	 * If value is not blank, original string value is automatically returned.
	 *
	 * @param value   String value.
	 * @param message Error message given to exception.
	 * @return Original string value.
	 * @throws java.lang.IllegalArgumentException if value is blank.
	 */
	public static String hasText(String value, String message) {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}
}
