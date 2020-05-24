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

package com.github.mjeanroy.springmvc.view.mustache.commons.reflection;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.ReflectionException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClassesTest {

	@Test
	public void it_should_check_if_class_is_available() {
		String className = getClass().getCanonicalName();
		assertThat(Classes.isPresent(className)).isTrue();
		assertThat(Classes.isPresent(className + "FooBar")).isFalse();
	}

	@Test
	public void it_should_instantiate_class() {
		Object instance = Classes.newInstance(FixtureClass.class.getName(), new Class<?>[]{}, new Object[]{});
		assertThat(instance).isNotNull();
		assertThat(instance).isExactlyInstanceOf(FixtureClass.class);
	}

	@Test
	public void it_should_fail_to_instantiate_unknown_class() {
		final String className = "foo_bar";
		final ThrowingCallable instantiate = new ThrowingCallable() {
			@Override
			public void call() {
				Classes.newInstance(className, new Class<?>[]{}, new Object[]{});
			}
		};

		assertThatThrownBy(instantiate).isInstanceOf(ReflectionException.class)
				.hasCauseInstanceOf(ClassNotFoundException.class)
				.hasMessage("java.lang.ClassNotFoundException: foo_bar");
	}

	@Test
	public void it_should_get_class_of_given_name() {
		String className = getClass().getName();
		Class<?> klass = Classes.getClassOf(className);
		assertThat(klass).isEqualTo(getClass());
	}

	@Test
	public void it_should_not_get_class_of_unknown_name() {
		final String className = "foo_bar";
		final ThrowingCallable getClassOf = new ThrowingCallable() {
			@Override
			public void call() {
				Classes.getClassOf(className);
			}
		};

		assertThatThrownBy(getClassOf)
				.isInstanceOf(ReflectionException.class)
				.hasCauseInstanceOf(ClassNotFoundException.class)
				.hasMessage("java.lang.ClassNotFoundException: foo_bar");
	}

	@Test
	public void it_should_invoke_method_on_given_class() {
		FixtureClass fixtureClass = new FixtureClass();
		Object[] args = {};
		Class<?>[] argTypes = {};
		Object result = Classes.invoke(fixtureClass, "emptyMethod", argTypes, args);

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
		Class<?>[] argTypes = {String.class};

		Object result = Classes.invoke(fixtureClass, "methodWithArgument", argTypes, args);

		assertThat(result).isNull();
		assertThat(fixtureClass.calls).contains(
				entry("emptyMethod", 0),
				entry("methodWithArgument", 1)
		);
	}

	@Test
	public void it_should_fail_to_invoke_unknown_method() {
		final FixtureClass fixtureClass = new FixtureClass();
		final Object[] args = {};
		final Class<?>[] argTypes = {};
		final String methodName = "method_does_not_exist";
		final ThrowingCallable invoke = new ThrowingCallable() {
			@Override
			public void call() {
				Classes.invoke(fixtureClass, methodName, argTypes, args);
			}
		};

		assertThatThrownBy(invoke)
				.isInstanceOf(ReflectionException.class)
				.hasCauseInstanceOf(NoSuchMethodException.class)
				.hasMessage("java.lang.NoSuchMethodException: com.github.mjeanroy.springmvc.view.mustache.commons.reflection.ClassesTest$FixtureClass.method_does_not_exist()");
	}

	@Test
	public void it_should_get_annotation_values() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("foo", "qux");

		String annotationName = Foo.class.getName();
		AnnotationMetadata metadata = mock(AnnotationMetadata.class);
		when(metadata.getAnnotationAttributes(annotationName)).thenReturn(attributes);

		String value = Classes.getAnnotationValue(metadata, Foo.class, "foo", "bar");

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

		String value = Classes.getAnnotationValue(metadata, Foo.class, "foo", "bar");

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

		String value = Classes.getAnnotationValue(metadata, Foo.class, "foo", "bar");

		verify(metadata).getAnnotationAttributes(annotationName);
		assertThat(value).isNull();
	}

	private @interface Foo {
	}

	private static class FixtureClass {
		private final Map<String, Integer> calls;

		public FixtureClass() {
			this.calls = new HashMap<String, Integer>();
			this.calls.put("emptyMethod", 0);
			this.calls.put("methodWithArgument", 0);
		}

		@SuppressWarnings("unused")
		public void emptyMethod() {
			this.increment("emptyMethod");
		}

		@SuppressWarnings("unused")
		public void methodWithArgument(String arg) {
			this.increment("methodWithArgument");
		}

		private void increment(String name) {
			this.calls.put(name, this.calls.get(name) + 1);
		}
	}
}
