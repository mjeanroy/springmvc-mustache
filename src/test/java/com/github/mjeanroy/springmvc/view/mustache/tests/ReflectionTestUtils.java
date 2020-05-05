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

package com.github.mjeanroy.springmvc.view.mustache.tests;

import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * Static Reflection Utilities, to use in unit test only.
 */
public final class ReflectionTestUtils {

	// Ensure non instantiation.
	private ReflectionTestUtils() {
	}

	/**
	 * Read field from given object instance.
	 * @param instance Object instance.
	 * @param fieldName Field name.
	 * @param <T> Type of given field.
	 * @return The field, may be {@code null}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readField(Object instance, String fieldName) {
		try {
			return (T) FieldUtils.readField(instance, fieldName, true);
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Read static field from given class.
	 * @param klass Class.
	 * @param fieldName Field name.
	 * @param <T> Type of given field.
	 * @return The field, may be {@code null}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readStaticField(Class<?> klass, String fieldName) {
		try {
			return (T) FieldUtils.readStaticField(klass, fieldName, true);
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}
}
