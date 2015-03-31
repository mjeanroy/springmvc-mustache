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

import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.hasText;

import java.util.Map;

import org.springframework.core.type.AnnotationMetadata;

/**
 * Commons static class utilities.
 */
public final class ClassUtils {

	private ClassUtils() {
	}

	/**
	 * Check that a given class is available on
	 * classpath.
	 *
	 * @param klass Class name.
	 * @return True if class is available, false otherwise.
	 */
	public static boolean isPresent(String klass) {
		hasText(klass, "Class name must not be empty");
		try {
			Class.forName(klass);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Get annotation method value.
	 *
	 * @param importingClassMetadata Metadata.
	 * @param annotationClass        Annotation class to look for.
	 * @param name                   Name of method.
	 * @param defaultValue           Default value if original value is null.
	 * @param <T>                    Type of returned value.
	 * @return Annotation value, or default value if original value is null.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAnnotationValue(AnnotationMetadata importingClassMetadata, Class annotationClass, String name, T defaultValue) {
		Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(annotationClass.getName());
		T value = attributes != null ? (T) attributes.get(name) : null;
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
}
