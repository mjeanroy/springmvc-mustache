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

package com.github.mjeanroy.springmvc.view.mustache.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;

class MustacheTemplateLoaderConfigurationTest {

	private MockEnvironment environment;
	private MustacheTemplateLoaderConfiguration templateLoaderConfiguration;

	@BeforeEach
	void setUp() {
		environment = new MockEnvironment();
		templateLoaderConfiguration = new MustacheTemplateLoaderConfiguration(environment);
	}

	@Test
	void it_should_create_template_loader() {
		MustacheTemplateLoaderFactoryBean factoryBean = templateLoaderConfiguration.mustacheTemplateLoader();
		assertThat(factoryBean).isNotNull();
		assertThat(readField(factoryBean, "prefix", String.class)).isEqualTo("/templates/");
		assertThat(readField(factoryBean, "suffix", String.class)).isEqualTo(".template.html");
	}

	@Test
	void it_should_create_template_loader_with_prefix_and_suffix() {
		String prefix = "/";
		String suffix = ".mustache";

		environment.setProperty("mustache.prefix", prefix);
		environment.setProperty("mustache.suffix", suffix);

		MustacheTemplateLoaderFactoryBean factoryBean = templateLoaderConfiguration.mustacheTemplateLoader();
		assertThat(factoryBean).isNotNull();
		assertThat(readField(factoryBean, "prefix", String.class)).isEqualTo(prefix);
		assertThat(readField(factoryBean, "suffix", String.class)).isEqualTo(suffix);
	}
}
