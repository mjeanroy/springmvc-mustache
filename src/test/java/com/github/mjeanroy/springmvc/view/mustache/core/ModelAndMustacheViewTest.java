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

package com.github.mjeanroy.springmvc.view.mustache.core;

import com.github.mjeanroy.springmvc.view.mustache.MustacheSettings;
import com.github.mjeanroy.springmvc.view.mustache.MustacheView;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustachePartialsMappingException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModelAndMustacheViewTest {

	@Test
	public void it_should_get_empty_partials() {
		ModelAndMustacheView view = new ModelAndMustacheView("viewName");

		Map<String, String> partials = view.getPartials();

		assertThat(partials).isNotNull().isEmpty();
	}

	@Test
	public void it_should_failed_if_current_partials_is_not_valid() {
		final ModelAndMustacheView view = new ModelAndMustacheView("viewName");
		view.addObject(MustacheSettings.PARTIALS_KEY, "foo");

		final ThrowingCallable getPartials = new ThrowingCallable() {
			@Override
			public void call() {
				view.getPartials();
			}
		};

		assertThatThrownBy(getPartials).isInstanceOf(MustachePartialsMappingException.class);
	}

	@Test
	public void it_should_add_partial() {
		String p1 = "foo";
		String v1 = "bar";

		ModelAndMustacheView view = new ModelAndMustacheView("viewName");

		view.addPartial(p1, v1);
		Map<String, String> currentPartials = view.getPartials();

		assertThat(currentPartials).isNotNull().hasSize(1).containsOnly(
				entry(p1, v1)
		);
	}

	@Test
	public void it_should_add_partials() {
		String p1 = "foo";
		String v1 = "bar";
		String p2 = "bar";
		String v2 = "foo";

		Map<String, String> partials = new HashMap<String, String>();
		partials.put(p1, v1);
		partials.put(p2, v2);

		ModelAndMustacheView view = new ModelAndMustacheView("viewName");

		view.addPartials(partials);
		Map<String, String> currentPartials = view.getPartials();

		assertThat(currentPartials).isNotNull().hasSize(partials.size()).containsOnly(
				entry(p1, v1),
				entry(p2, v2)
		);
	}

	@Test
	public void it_should_add_partial_to_view() {
		String p1 = "foo";
		String v1 = "bar";

		MustacheView mustacheView = mock(MustacheView.class);
		ModelAndMustacheView view = new ModelAndMustacheView(mustacheView);

		view.addPartial(p1, v1);

		verify(mustacheView).addAlias(p1, v1);
		assertThat(view.getModelMap()).isNotNull().isEmpty();
	}

	@Test
	public void it_should_add_partials_to_view() {
		String p1 = "foo";
		String v1 = "bar";

		String p2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(p1, v1);
		aliases.put(p2, v2);

		MustacheView mustacheView = mock(MustacheView.class);
		ModelAndMustacheView view = new ModelAndMustacheView(mustacheView);

		view.addPartials(aliases);

		verify(mustacheView).addAliases(aliases);
		assertThat(view.getModelMap()).isNotNull().isEmpty();
	}

	@Test
	public void it_should_get_partials_from_view() {
		String p1 = "foo";
		String v1 = "bar";

		String p2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(p1, v1);
		aliases.put(p2, v2);

		MustacheView mustacheView = mock(MustacheView.class);
		when(mustacheView.getAliases()).thenReturn(aliases);

		ModelAndMustacheView view = new ModelAndMustacheView(mustacheView);

		Map<String, String> partials = view.getPartials();

		verify(mustacheView).getAliases();
		assertThat(partials).isNotNull().isSameAs(aliases);
	}
}
