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

package com.github.mjeanroy.springmvc.view.mustache.commons;

import org.junit.Test;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClassUtilsTest {

	@Test
	public void it_should_check_if_class_is_available() {
		String className = getClass().getCanonicalName();
		assertThat(ClassUtils.isPresent(className)).isTrue();
		assertThat(ClassUtils.isPresent(className + "FooBar")).isFalse();
	}

	@Test
	public void it_should_instantiate_class() {
		Object instance = ClassUtils.newInstance(FixtureClass.class.getName());
		assertThat(instance).isNotNull();
		assertThat(instance).isExactlyInstanceOf(FixtureClass.class);
	}

	@Test
	public void it_should_invoke_method_on_given_class() {
		FixtureClass fixtureClass = new FixtureClass();
		Object[] args = {};
		Object result = ClassUtils.invoke(fixtureClass, "emptyMethod", args);

		assertThat(result).isNull();
		assertThat(fixtureClass.calls).contains(
				entry("emptyMethod", 1),
				entry("methodWithArgument", 0)
		);
	}

	@Test
	public void it_should_invoke_method_with_argument_on_given_class() {
		FixtureClass fixtureClass = new FixtureClass();
		Object[] args = {"test"};
		Object result = ClassUtils.invoke(fixtureClass, "methodWithArgument", args);

		assertThat(result).isNull();
		assertThat(fixtureClass.calls).contains(
				entry("emptyMethod", 0),
				entry("methodWithArgument", 1)
		);
	}

	@Test
	public void it_should_get_annotation_values() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("foo", "qux");

		String annotationName = Foo.class.getName();
		AnnotationMetadata metadata = mock(AnnotationMetadata.class);
		when(metadata.getAnnotationAttributes(annotationName)).thenReturn(attributes);

		String value = ClassUtils.getAnnotationValue(metadata, Foo.class, "foo", "bar");

		verify(metadata).getAnnotationAttributes(annotationName);
		assertThat(value)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo("qux");
	}

	@Test
	public void it_should_get_default_annotation_values() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		String annotationName = Foo.class.getName();
		AnnotationMetadata metadata = mock(AnnotationMetadata.class);
		when(metadata.getAnnotationAttributes(annotationName)).thenReturn(attributes);

		String value = ClassUtils.getAnnotationValue(metadata, Foo.class, "foo", "bar");

		verify(metadata).getAnnotationAttributes(annotationName);
		assertThat(value)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo("bar");
	}

	@Test
	public void it_should_get_null_value() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("foo", null);

		String annotationName = Foo.class.getName();
		AnnotationMetadata metadata = mock(AnnotationMetadata.class);
		when(metadata.getAnnotationAttributes(annotationName)).thenReturn(attributes);

		String value = ClassUtils.getAnnotationValue(metadata, Foo.class, "foo", "bar");

		verify(metadata).getAnnotationAttributes(annotationName);
		assertThat(value).isNull();
	}

	private @interface Foo {
	}

	private static class FixtureClass {
		private Map<String, Integer> calls;

		public FixtureClass() {
			this.calls = new HashMap<String, Integer>();
			this.calls.put("emptyMethod", 0);
			this.calls.put("methodWithArgument", 0);
		}

		public void emptyMethod() {
			this.increment("emptyMethod");
		}

		public void methodWithArgument(String arg) {
			this.increment("methodWithArgument");
		}

		private void increment(String name) {
			this.calls.put(name, this.calls.get(name) + 1);
		}
	}
}
