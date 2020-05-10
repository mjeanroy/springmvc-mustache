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
import com.github.mjeanroy.junit4.runif.RunIf;
import com.github.mjeanroy.junit4.runif.RunIfRunner;
import com.github.mjeanroy.junit4.runif.conditions.AtLeastJava8Condition;
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils;
import com.samskivert.mustache.Mustache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RunIfRunner.class)
public class MustacheProviderTest {

	@Test
	public void it_should_get_jmustache_configuration_class() {
		MustacheProvider provider = MustacheProvider.JMUSTACHE;
		String className = "com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheConfiguration";
		it_should_get_provider_configuration_class(provider, className);
	}

	@Test
	public void it_should_instantiate_jmustache_engine_compiler() {
		Mustache.Compiler compiler = Mustache.compiler();
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getBean(Mustache.Compiler.class)).thenReturn(compiler);

		MustacheProvider provider = MustacheProvider.JMUSTACHE;
		String className = "com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler";
		it_should_instantiate_engine_compiler(provider, applicationContext, className);
	}

	@Test
	public void it_should_get_mustachejava_configuration_class() {
		MustacheProvider provider = MustacheProvider.MUSTACHE_JAVA;
		String className = "com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaConfiguration";
		it_should_get_provider_configuration_class(provider, className);
	}

	@Test
	public void it_should_instantiate_mustachejava_engine_compiler() {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		MustacheProvider provider = MustacheProvider.MUSTACHE_JAVA;
		String className = "com.github.mjeanroy.springmvc.view.mustache.mustachejava.MustacheJavaCompiler";
		it_should_instantiate_engine_compiler(provider, applicationContext, className);
	}

	@Test
	public void it_should_get_handlebars_configuration_class() {
		MustacheProvider provider = MustacheProvider.HANDLEBARS;
		String className = "com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsConfiguration";
		it_should_get_provider_configuration_class(provider, className);
	}

	@Test
	public void it_should_instantiate_handlebars_engine_compiler() {
		Handlebars handlebars = new Handlebars();
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getBean(Handlebars.class)).thenReturn(handlebars);

		MustacheProvider provider = MustacheProvider.HANDLEBARS;
		String className = "com.github.mjeanroy.springmvc.view.mustache.handlebars.HandlebarsCompiler";
		it_should_instantiate_engine_compiler(provider, applicationContext, className);
	}

	@Test
	public void it_should_get_auto_configuration_class() {
		MustacheProvider provider = MustacheProvider.AUTO;
		String className = "com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheConfiguration";
		it_should_get_provider_configuration_class(provider, className);
	}

	@Test
	public void it_should_instantiate_auto_engine_compiler() {
		Mustache.Compiler compiler = Mustache.compiler();
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getBean(Mustache.Compiler.class)).thenReturn(compiler);

		MustacheProvider provider = MustacheProvider.JMUSTACHE;
		String className = "com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler";
		it_should_instantiate_engine_compiler(provider, applicationContext, className);
	}

	@Test
	@RunIf(AtLeastJava8Condition.class)
	public void it_should_get_nashorn_configuration_class() {
		MustacheProvider provider = MustacheProvider.NASHORN;
		String className = "com.github.mjeanroy.springmvc.view.mustache.configuration.nashorn.NashornConfiguration";
		it_should_get_provider_configuration_class(provider, className);
	}

	@Test
	@RunIf(AtLeastJava8Condition.class)
	public void it_should_instantiate_nashorn_engine_compiler() {
		final ResourceLoader resourceLoader = new DefaultResourceLoader();
		final MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		final Object compiler = ReflectionTestUtils.instantiate("com.github.mjeanroy.springmvc.view.mustache.nashorn.MustacheEngine", new Class<?>[]{MustacheTemplateLoader.class}, new Object[]{templateLoader});
		final ApplicationContext applicationContext = mock(ApplicationContext.class);

		when(applicationContext.getBean(compiler.getClass())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				return compiler;
			}
		});

		MustacheProvider provider = MustacheProvider.NASHORN;
		String className = "com.github.mjeanroy.springmvc.view.mustache.nashorn.NashornCompiler";
		it_should_instantiate_engine_compiler(provider, applicationContext, className);
	}

	private void it_should_get_provider_configuration_class(MustacheProvider provider, String className) {
		assertThat(provider.configurationClass()).isEqualTo(className);
	}

	private void it_should_instantiate_engine_compiler(MustacheProvider provider, ApplicationContext applicationContext, String className) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		when(applicationContext.getBean(MustacheTemplateLoader.class)).thenReturn(templateLoader);

		MustacheCompiler mustacheCompiler = provider.instantiate(applicationContext);
		assertThat(mustacheCompiler).isNotNull();
		assertThat(mustacheCompiler.getClass().getName()).isEqualTo(className);
	}
}
