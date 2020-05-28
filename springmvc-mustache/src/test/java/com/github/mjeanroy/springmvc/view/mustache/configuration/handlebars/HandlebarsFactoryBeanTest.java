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

package com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class HandlebarsFactoryBeanTest {

	private HandlebarsFactoryBean factoryBean;

	@BeforeEach
	void setUp() {
		factoryBean = new HandlebarsFactoryBean();
	}

	@Test
	void it_should_create_factory_bean_as_singleton() {
		assertThat(factoryBean.isSingleton()).isTrue();
	}

	@Test
	void it_should_create_factory_bean_with_target_class() {
		assertThat(factoryBean.getObjectType()).isEqualTo(Handlebars.class);
	}

	@Test
	void it_should_create_target_object_with_default_settings() throws Exception {
		factoryBean.afterPropertiesSet();

		Handlebars handlebars = factoryBean.getObject();

		assertThat(handlebars).isNotNull();
	}

	@Test
	void it_should_not_create_target_object_twice() throws Exception {
		factoryBean.afterPropertiesSet();

		Handlebars h1 = factoryBean.getObject();
		Handlebars h2 = factoryBean.getObject();

		assertThat(h1).isNotNull();
		assertThat(h2).isNotNull();
		assertThat(h1).isSameAs(h2);
	}

	@Test
	void it_should_create_instance_with_default_properties() throws Exception {
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();

		String rawTemplate = "Hello {{name}}";
		Template template = handlebars.compileInline(rawTemplate);
		Map<String, String> model = Collections.singletonMap("name", "John Doe");

		assertThat(template.apply(model)).isEqualTo("Hello John Doe");
		assertThat(handlebars.stringParams()).isFalse();
		assertThat(handlebars.infiniteLoops()).isFalse();
		assertThat(handlebars.deletePartialAfterMerge()).isFalse();
		assertThat(handlebars.parentScopeResolution()).isTrue();
		assertThat(handlebars.prettyPrint()).isFalse();
	}

	@Test
	void it_should_create_instance_with_start_and_end_delimiter_properties() throws Exception {
		factoryBean.setStartDelimiter("[[");
		factoryBean.setEndDelimiter("]]");
		factoryBean.afterPropertiesSet();

		Handlebars handlebars = factoryBean.getObject();

		String rawTemplate = "Hello [[name]]";
		Template template = handlebars.compileInline(rawTemplate);
		Map<String, String> model = Collections.singletonMap("name", "John Doe");

		assertThat(template.apply(model)).isEqualTo("Hello John Doe");
	}

	@Test
	void it_should_create_instance_with_with_string_param_property() throws Exception {
		factoryBean.setStringParams(true);
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();
		assertThat(handlebars.stringParams()).isTrue();
	}

	@Test
	void it_should_create_instance_with_infinite_loop_property() throws Exception {
		factoryBean.setInfiniteLoops(true);
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();
		assertThat(handlebars.infiniteLoops()).isTrue();
	}

	@Test
	void it_should_create_instance_with_delete_partials_after_merge_property() throws Exception {
		factoryBean.setDeletePartialAfterMerge(true);
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();
		assertThat(handlebars.deletePartialAfterMerge()).isTrue();
	}

	@Test
	void it_should_create_instance_with_parent_scope_resolution_property() throws Exception {
		factoryBean.setParentScopeResolution(false);
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();
		assertThat(handlebars.parentScopeResolution()).isFalse();
	}

	@Test
	void it_should_create_instance_with_pretty_print_property() throws Exception {
		factoryBean.setPrettyPrint(true);
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();
		assertThat(handlebars.prettyPrint()).isTrue();
	}

	@Test
	void it_should_create_instance_using_given_customizer() throws Exception {
		HandlebarsCustomizer c1 = newHandlebarsCustomizer();
		HandlebarsCustomizer c2 = newHandlebarsCustomizer();
		List<HandlebarsCustomizer> customizers = asList(c1, c2);

		factoryBean.setCustomizers(customizers);
		factoryBean.afterPropertiesSet();

		Handlebars handlebars = factoryBean.getObject();

		InOrder inOrder = inOrder(c1, c2);
		inOrder.verify(c1).customize(handlebars);
		inOrder.verify(c2).customize(handlebars);
	}

	private static HandlebarsCustomizer newHandlebarsCustomizer() {
		return mock(HandlebarsCustomizer.class);
	}
}
