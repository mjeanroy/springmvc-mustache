/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 <mickael.jeanroy@gmail.com>
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.hasText;
import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notEmpty;
import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notNull;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class PreConditionsTest {

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_not_throw_null_pointer_exception() {
		String param = "foo";
		String result = notNull(param, "bar");
		assertThat(result).isSameAs(param);
	}

	@Test
	public void it_should_throw_null_pointer_exception() {
		String message = "foo must not be null";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);
		notNull(null, message);
	}

	@Test
	public void it_should_not_throw_illegal_argument_exception_if_array_is_not_empty() {
		String[] array = new String[]{"foo", "bar"};
		String[] result = notEmpty(array, "Array must not be empty");
		assertThat(result).isSameAs(array);
	}

	@Test
	public void it_should_throw_illegal_argument_exception_if_array_is_not_empty() {
		String message = "Array must not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);
		notEmpty(new String[]{}, message);
	}

	@Test
	public void it_should_not_throw_illegal_argument_exception_if_collection_is_not_empty() {
		Collection<String> collection = asList("foo", "bar");
		Collection<String> result = notEmpty(collection, "Collection must not be empty");
		assertThat(result).isSameAs(collection);
	}

	@Test
	public void it_should_throw_illegal_argument_exception_if_collection_is_not_empty() {
		String message = "Collection must not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);
		notEmpty(emptyList(), message);
	}

	@Test
	public void it_should_not_throw_illegal_argument_exception_if_string_has_text() {
		String param = "foo";
		String result = hasText(param, "bar");
		assertThat(result).isSameAs(param);
	}

	@Test
	public void it_should_throw_illegal_argument_exception_if_string_does_not_have_text() {
		String message = "String must have text";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);
		hasText("  ", message);
	}
}
