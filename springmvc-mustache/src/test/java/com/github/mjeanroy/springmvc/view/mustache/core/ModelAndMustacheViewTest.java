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
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

class ModelAndMustacheViewTest {

	@Test
	void it_should_create_empty_view() {
		ModelAndMustacheView view = new ModelAndMustacheView();
		assertThat(view.getView()).isNull();
		assertThat(view.getModel()).isEmpty();
		assertThat(view.getPartials()).isEmpty();
	}

	@Test
	void it_should_create_view_with_name_and_model() {
		String viewName = "view";

		Map<String, Object> model = new HashMap<>();
		model.put("firstName", "John");
		model.put("lastName", "Doe");

		ModelAndMustacheView view = new ModelAndMustacheView(viewName, model);

		assertThat(view.getViewName()).isEqualTo(viewName);
		assertThat(view.getModel()).isEqualTo(model);
		assertThat(view.getPartials()).isEmpty();
	}

	@Test
	void it_should_create_view_with_name_and_model_with_name() {
		String viewName = "view";
		String modelName = "model";

		Map<String, Object> model = new HashMap<>();
		model.put("firstName", "John");
		model.put("lastName", "Doe");

		ModelAndMustacheView view = new ModelAndMustacheView(viewName, modelName, model);

		assertThat(view.getViewName()).isEqualTo(viewName);
		assertThat(view.getModel()).hasSize(1);
		assertThat(view.getModel().get(modelName)).isEqualTo(model);
		assertThat(view.getPartials()).isEmpty();
	}

	@Test
	void it_should_get_empty_partials() {
		ModelAndMustacheView view = new ModelAndMustacheView("viewName");
		Map<String, String> partials = view.getPartials();
		assertThat(partials).isNotNull().isEmpty();
	}

	@Test
	void it_should_failed_if_current_partials_is_not_valid() {
		ModelAndMustacheView view = new ModelAndMustacheView("viewName");
		view.addObject(MustacheSettings.PARTIALS_KEY, "foo");

		assertThatThrownBy(view::getPartials).isInstanceOf(MustachePartialsMappingException.class);
	}

	@Test
	void it_should_add_partial() {
		String p1 = "foo";
		String v1 = "bar";
		ModelAndMustacheView view = new ModelAndMustacheView("viewName");

		view.addPartial(p1, v1);

		assertThat(view.getPartials()).hasSize(1).containsOnly(
				entry(p1, v1)
		);
	}

	@Test
	void it_should_add_partials() {
		String p1 = "foo";
		String v1 = "bar";
		String p2 = "bar";
		String v2 = "foo";

		Map<String, String> partials = new HashMap<>();
		partials.put(p1, v1);
		partials.put(p2, v2);

		ModelAndMustacheView view = new ModelAndMustacheView("viewName");

		view.addPartials(partials);

		assertThat(view.getPartials()).hasSize(partials.size()).containsOnly(
				entry(p1, v1),
				entry(p2, v2)
		);
	}

	@Test
	void it_should_add_partial_to_view() {
		String p1 = "foo";
		String v1 = "bar";

		MustacheView mustacheView = new MustacheView();
		ModelAndMustacheView view = new ModelAndMustacheView(mustacheView);

		view.addPartial(p1, v1);

		assertThat(mustacheView.getAliases()).hasSize(1).contains(entry(p1, v1));
		assertThat(view.getModelMap()).isNotNull().isEmpty();
	}

	@Test
	void it_should_add_partials_to_view() {
		Map<String, String> aliases = new HashMap<>();
		aliases.put("foo", "bar");
		aliases.put("bar", "foo");

		MustacheView mustacheView = new MustacheView();
		ModelAndMustacheView view = new ModelAndMustacheView(mustacheView);

		view.addPartials(aliases);

		assertThat(mustacheView.getAliases()).isEqualTo(aliases);
		assertThat(view.getModelMap()).isEmpty();
	}

	@Test
	void it_should_get_partials_from_view() {
		Map<String, String> aliases = new HashMap<>();
		aliases.put("foo", "bar");
		aliases.put("bar", "foo");

		MustacheView mustacheView = new MustacheView();
		mustacheView.addAliases(aliases);

		ModelAndMustacheView view = new ModelAndMustacheView(mustacheView);

		Map<String, String> partials = view.getPartials();

		assertThat(partials).isEqualTo(aliases);
	}
}
