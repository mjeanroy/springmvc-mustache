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

package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.samskivert.mustache.Mustache;
import org.junit.Before;
import org.junit.Test;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class JMustacheConfigurationTest {

	private JMustacheConfiguration jMustacheConfiguration;

	@Before
	public void setUp() {
		jMustacheConfiguration = new JMustacheConfiguration();
	}

	@Test
	public void it_should_instantiate_mustache_compiler() {
		Mustache.Compiler compiler = mock(Mustache.Compiler.class);
		MustacheTemplateLoader templateLoader = mock(MustacheTemplateLoader.class);
		MustacheCompiler mustacheCompiler = jMustacheConfiguration.mustacheCompiler(compiler, templateLoader);
		assertThat(mustacheCompiler).isNotNull();
	}

	@Test
	public void it_should_create_mustache_compiler_factory_bean() {
		JMustacheCompilerFactoryBean factoryBean = jMustacheConfiguration.jMustacheCompiler();

		String nullValue = readField(factoryBean, "nullValue");
		String defaultValue = readField(factoryBean, "defaultValue");
		boolean emptyStringIsFalse = readField(factoryBean, "emptyStringIsFalse");
		boolean zeroIsFalse = readField(factoryBean, "zeroIsFalse");
		boolean escapeHTML = readField(factoryBean, "escapeHTML");

		assertThat(nullValue).isEqualTo("");
		assertThat(defaultValue).isEqualTo("");
		assertThat(emptyStringIsFalse).isTrue();
		assertThat(zeroIsFalse).isTrue();
		assertThat(escapeHTML).isTrue();
	}
}
