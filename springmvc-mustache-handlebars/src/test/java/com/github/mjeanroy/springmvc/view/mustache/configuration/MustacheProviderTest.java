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

import com.github.jknack.handlebars.Handlebars;
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.handlebars.HandlebarsCompiler;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MustacheProviderTest {

	@Test
	public void it_should_support_handlebars() {
		assertThat(MustacheProvider.HANDLEBARS.isAvailable()).isTrue();
	}

	@Test
	public void it_should_get_handlebars_configuration() {
		assertThat(MustacheProvider.HANDLEBARS.configurationClass()).isEqualTo(HandlebarsConfiguration.class.getName());
	}

	@Test
	public void it_should_instantiate_handlebars_compiler() throws Exception {
		verifyProvider(MustacheProvider.HANDLEBARS);
	}

	@Test
	public void it_should_support_auto_configuration() {
		assertThat(MustacheProvider.AUTO.isAvailable()).isTrue();
	}

	@Test
	public void it_should_get_auto_configuration() {
		assertThat(MustacheProvider.AUTO.configurationClass()).isEqualTo(HandlebarsConfiguration.class.getName());
	}

	@Test
	public void it_should_instantiate_auto_compiler() throws Exception {
		verifyProvider(MustacheProvider.AUTO);
	}

	private void verifyProvider(MustacheProvider provider) throws Exception {
		Handlebars hbs = new Handlebars();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);

		ApplicationContext applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getBean(Handlebars.class)).thenReturn(hbs);
		when(applicationContext.getBean(MustacheTemplateLoader.class)).thenReturn(templateLoader);

		MustacheCompiler mustacheCompiler = provider.instantiate(applicationContext);
		assertThat(mustacheCompiler).isInstanceOf(HandlebarsCompiler.class);
	}

	@Test
	public void it_should_not_support_mustachejava() {
		assertThat(MustacheProvider.MUSTACHE_JAVA.isAvailable()).isFalse();
	}

	@Test
	public void it_should_support_jmustache() {
		assertThat(MustacheProvider.JMUSTACHE.isAvailable()).isFalse();
	}
}
