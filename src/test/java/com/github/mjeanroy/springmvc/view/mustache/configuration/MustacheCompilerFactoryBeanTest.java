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
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.samskivert.mustache.Mustache;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MustacheCompilerFactoryBeanTest {

	private MustacheCompilerFactoryBean factoryBean;

	private ApplicationContext applicationContext;

	@Before
	public void setUp() {
		applicationContext = mock(ApplicationContext.class);

		factoryBean = new MustacheCompilerFactoryBean();
		factoryBean.setApplicationContext(applicationContext);
	}

	@Test
	public void it_should_be_a_singleton() {
		assertThat(factoryBean.isSingleton()).isTrue();
	}

	@Test
	public void it_should_have_object_type() {
		assertThat(factoryBean.getObjectType()).isEqualTo(MustacheCompiler.class);
	}

	@Test
	public void it_should_create_object() throws Exception {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		Mustache.Compiler compiler = Mustache.compiler();
		Handlebars handlebars = new Handlebars();

		when(applicationContext.getBean(MustacheTemplateLoader.class)).thenReturn(templateLoader);
		when(applicationContext.getBean(Mustache.Compiler.class)).thenReturn(compiler);
		when(applicationContext.getBean(Handlebars.class)).thenReturn(handlebars);

		factoryBean.afterPropertiesSet();

		MustacheCompiler mustacheCompiler = factoryBean.getObject();

		assertThat(mustacheCompiler).isNotNull();

		verify(applicationContext).getBean(MustacheTemplateLoader.class);
	}

	@Test
	public void it_should_not_create_twice() throws Exception {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		Mustache.Compiler compiler = Mustache.compiler();
		Handlebars handlebars = new Handlebars();

		when(applicationContext.getBean(MustacheTemplateLoader.class)).thenReturn(templateLoader);
		when(applicationContext.getBean(Mustache.Compiler.class)).thenReturn(compiler);
		when(applicationContext.getBean(Handlebars.class)).thenReturn(handlebars);

		factoryBean.afterPropertiesSet();

		MustacheCompiler mustacheCompiler1 = factoryBean.getObject();
		MustacheCompiler mustacheCompiler2 = factoryBean.getObject();

		assertThat(mustacheCompiler1).isNotNull();
		assertThat(mustacheCompiler2).isNotNull();
		assertThat(mustacheCompiler1).isSameAs(mustacheCompiler2);
	}
}
