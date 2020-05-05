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

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PreConditionsTest {

	@Test
	public void it_should_not_throw_null_pointer_exception() {
		String param = "foo";
		String result = PreConditions.notNull(param, "bar");
		assertThat(result).isSameAs(param);
	}

	@Test
	public void it_should_throw_null_pointer_exception() {
		final String message = "foo must not be null";
		final ThrowingCallable notNull = new ThrowingCallable() {
			@Override
			public void call() {
				PreConditions.notNull(null, message);
			}
		};

		assertThatThrownBy(notNull)
				.isInstanceOf(NullPointerException.class)
				.hasMessage(message);
	}

	@Test
	public void it_should_not_throw_illegal_argument_exception_if_array_is_not_empty() {
		String[] array = new String[]{"foo", "bar"};
		String[] result = PreConditions.notEmpty(array, "Array must not be empty");
		assertThat(result).isSameAs(array);
	}

	@Test
	public void it_should_throw_illegal_argument_exception_if_array_is_not_empty() {
		final String message = "Array must not be empty";
		final ThrowingCallable notEmpty = new ThrowingCallable() {
			@Override
			public void call() {
				PreConditions.notEmpty(new String[]{}, message);
			}
		};

		assertThatThrownBy(notEmpty)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(message);
	}

	@Test
	public void it_should_not_throw_illegal_argument_exception_if_collection_is_not_empty() {
		Collection<String> collection = asList("foo", "bar");
		Collection<String> result = PreConditions.notEmpty(collection, "Collection must not be empty");
		assertThat(result).isSameAs(collection);
	}

	@Test
	public void it_should_throw_illegal_argument_exception_if_collection_is_not_empty() {
		final String message = "Collection must not be empty";
		final ThrowingCallable notEmpty = new ThrowingCallable() {
			@Override
			public void call() {
				PreConditions.notEmpty(emptyList(), message);
			}
		};

		assertThatThrownBy(notEmpty)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(message);
	}

	@Test
	public void it_should_not_throw_illegal_argument_exception_if_string_has_text() {
		String param = "foo";
		String result = PreConditions.hasText(param, "bar");
		assertThat(result).isSameAs(param);
	}

	@Test
	public void it_should_throw_illegal_argument_exception_if_string_does_not_have_text() {
		final String message = "String must have text";
		final ThrowingCallable hasText = new ThrowingCallable() {
			@Override
			public void call() throws Throwable {
				PreConditions.hasText("  ", message);
			}
		};

		assertThatThrownBy(hasText)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(message);
	}
}
