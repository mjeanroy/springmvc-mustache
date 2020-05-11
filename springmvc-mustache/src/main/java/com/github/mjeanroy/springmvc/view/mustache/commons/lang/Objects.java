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

package com.github.mjeanroy.springmvc.view.mustache.commons.lang;

/**
 * Static Object Utilities.
 */
public final class Objects {

	private Objects() {
	}

	/**
	 * Check, in a null-safe way, that o1 equals o2.
	 *
	 * @param o1 First object to check.
	 * @param o2 Second object to check.
	 * @return {@code true} if {@code o1} equals {@code o2}, {@code false} otherwise.
	 */
	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}

		if (o1 == null || o2 == null) {
			return false;
		}

		return o1.equals(o2);
	}

	/**
	 * Compute hashcode of given objects.
	 *
	 * @param objects List of objects.
	 * @return Hash code value.
	 */
	public static int hashCode(Object... objects) {
		if (objects == null) {
			return 0;
		}

		int result = 1;

		for (Object element : objects) {
			result = 31 * result + (element == null ? 0 : element.hashCode());
		}

		return result;
	}
}
