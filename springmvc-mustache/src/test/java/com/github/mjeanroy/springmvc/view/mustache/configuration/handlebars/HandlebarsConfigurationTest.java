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
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.env.MockEnvironment;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class HandlebarsConfigurationTest {

	private MockEnvironment environment;
	private HandlebarsConfiguration handlebarConfiguration;

	@BeforeEach
	void setUp() {
		environment = new MockEnvironment();
		handlebarConfiguration = new HandlebarsConfiguration(environment);
	}

	@Test
	void it_should_instantiate_mustache_compiler() {
		Handlebars handlebars = new Handlebars();
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheCompiler mustacheCompiler = handlebarConfiguration.mustacheCompiler(handlebars, templateLoader);

		assertThat(mustacheCompiler).isNotNull();
	}

	@Test
	void it_should_instantiate_with_default_properties() throws Exception {
		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
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
	void it_should_instantiate_with_start_and_end_delimiter_properties() throws Exception {
		environment.setProperty("mustache.handlebars.startDelimiter", "[[");
		environment.setProperty("mustache.handlebars.endDelimiter", "]]");

		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();

		String rawTemplate = "Hello [[name]]";
		Template template = handlebars.compileInline(rawTemplate);
		Map<String, String> model = Collections.singletonMap("name", "John Doe");

		assertThat(template.apply(model)).isEqualTo("Hello John Doe");
	}

	@Test
	void it_should_instantiate_with_string_param_property() throws Exception {
		environment.setProperty("mustache.handlebars.stringParams", "true");

		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();

		assertThat(handlebars.stringParams()).isTrue();
	}

	@Test
	void it_should_instantiate_with_infinite_loop_property() throws Exception {
		environment.setProperty("mustache.handlebars.infiniteLoops", "true");

		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();

		assertThat(handlebars.infiniteLoops()).isTrue();
	}

	@Test
	void it_should_instantiate_with_delete_partials_after_merge_property() throws Exception {
		environment.setProperty("mustache.handlebars.deletePartialAfterMerge", "true");

		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();

		assertThat(handlebars.deletePartialAfterMerge()).isTrue();
	}

	@Test
	void it_should_instantiate_with_parent_scope_resolution_property() throws Exception {
		environment.setProperty("mustache.handlebars.parentScopeResolution", "false");

		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();

		assertThat(handlebars.parentScopeResolution()).isFalse();
	}

	@Test
	void it_should_instantiate_with_pretty_print_property() throws Exception {
		environment.setProperty("mustache.handlebars.prettyPrint", "true");

		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
		factoryBean.afterPropertiesSet();
		Handlebars handlebars = factoryBean.getObject();

		assertThat(handlebars.prettyPrint()).isTrue();
	}

	@Test
	void it_should_instantiate_with_handlebars_customizers() throws Exception {
		HandlebarsCustomizer c1 = newHandlebarsCustomizer();
		HandlebarsCustomizer c2 = newHandlebarsCustomizer();
		List<HandlebarsCustomizer> customizers = asList(c1, c2);

		handlebarConfiguration.setCustomizers(customizers);

		HandlebarsFactoryBean factoryBean = handlebarConfiguration.handlebarsCompiler();
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
