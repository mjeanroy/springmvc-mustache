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

package com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

public class MustacheJavaConfigurationTest {

	private MockEnvironment environment;
	private MustacheJavaConfiguration mustacheJavaConfiguration;

	@Before
	public void setUp() {
		environment = new MockEnvironment();
		mustacheJavaConfiguration = new MustacheJavaConfiguration(environment);
	}

	@Test
	public void it_should_instantiate_mustache_compiler() {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		SpringMustacheFactory mustacheFactory = new SpringMustacheFactory(templateLoader);
		MustacheCompiler mustacheCompiler = mustacheJavaConfiguration.mustacheCompiler(mustacheFactory, templateLoader);
		assertThat(mustacheCompiler).isNotNull();
	}

	@Test
	public void it_should_instantiate_mustache_factory() {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheFactory mustacheFactory = mustacheJavaConfiguration.mustacheFactory(templateLoader);

		assertThat(mustacheFactory).isInstanceOf(SpringMustacheFactory.class);
		assertThat(((SpringMustacheFactory) mustacheFactory).getRecursionLimit()).isEqualTo(100);
	}

	@Test
	public void it_should_instantiate_mustache_factory_with_custom_recursion_limit() {
		environment.setProperty("mustache.mustachejava.recursionLimit", "10");

		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheFactory mustacheFactory = mustacheJavaConfiguration.mustacheFactory(templateLoader);

		assertThat(mustacheFactory).isInstanceOf(SpringMustacheFactory.class);
		assertThat(((SpringMustacheFactory) mustacheFactory).getRecursionLimit()).isEqualTo(10);
	}
}
