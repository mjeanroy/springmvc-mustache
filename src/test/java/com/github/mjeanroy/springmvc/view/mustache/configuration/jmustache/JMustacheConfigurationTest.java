/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014, 2015 <mickael.jeanroy@gmail.com>
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
import com.github.mjeanroy.springmvc.view.mustache.core.CompositeResourceLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.samskivert.mustache.Mustache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class JMustacheConfigurationTest {

	@InjectMocks
	private JMustacheConfiguration jMustacheConfiguration;

	@Test
	public void it_should_instantiate_template_loader() {
		MustacheTemplateLoader templateLoader = jMustacheConfiguration.mustacheTemplateLoader();
		assertThat(templateLoader).isNotNull();
	}

	@Test
	public void it_should_instantiate_template_loader_using_unique_loaders() throws Exception {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		writeField(jMustacheConfiguration, "applicationContext", applicationContext, true);
		writeField(jMustacheConfiguration, "resourceLoader", applicationContext, true);

		MustacheTemplateLoader templateLoader = jMustacheConfiguration.mustacheTemplateLoader();

		assertThat(templateLoader)
				.isNotNull()
				.isExactlyInstanceOf(DefaultTemplateLoader.class);

		DefaultTemplateLoader defaultTemplateLoader = (DefaultTemplateLoader) templateLoader;
		ResourceLoader resourceLoader = (ResourceLoader) readField(defaultTemplateLoader, "resourceLoader", true);
		assertThat(resourceLoader)
				.isNotNull()
				.isExactlyInstanceOf(CompositeResourceLoader.class);

		CompositeResourceLoader compositeResourceLoader = (CompositeResourceLoader) resourceLoader;

		@SuppressWarnings("unchecked")
		Collection<ResourceLoader> loaders = (Collection<ResourceLoader>) readField(compositeResourceLoader, "resourceLoaders", true);

		assertThat(loaders)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.containsOnly(applicationContext);
	}

	@Test
	public void it_should_instantiate_template_loader_using_given_loaders() throws Exception {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		ResourceLoader resourceLoader = mock(ResourceLoader.class);

		writeField(jMustacheConfiguration, "applicationContext", applicationContext, true);
		writeField(jMustacheConfiguration, "resourceLoader", resourceLoader, true);

		MustacheTemplateLoader templateLoader = jMustacheConfiguration.mustacheTemplateLoader();

		assertThat(templateLoader)
				.isNotNull()
				.isExactlyInstanceOf(DefaultTemplateLoader.class);

		DefaultTemplateLoader defaultTemplateLoader = (DefaultTemplateLoader) templateLoader;
		ResourceLoader rl = (ResourceLoader) readField(defaultTemplateLoader, "resourceLoader", true);
		assertThat(rl)
				.isNotNull()
				.isExactlyInstanceOf(CompositeResourceLoader.class);

		CompositeResourceLoader compositeResourceLoader = (CompositeResourceLoader) rl;

		@SuppressWarnings("unchecked")
		Collection<ResourceLoader> loaders = (Collection<ResourceLoader>) readField(compositeResourceLoader, "resourceLoaders", true);

		assertThat(loaders)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.containsOnly(
						applicationContext,
						resourceLoader
				);
	}

	@Test
	public void it_should_instantiate_template_loader_using_default_loaders() throws Exception {
		writeField(jMustacheConfiguration, "applicationContext", null, true);
		writeField(jMustacheConfiguration, "resourceLoader", null, true);

		MustacheTemplateLoader templateLoader = jMustacheConfiguration.mustacheTemplateLoader();

		assertThat(templateLoader)
				.isNotNull()
				.isExactlyInstanceOf(DefaultTemplateLoader.class);

		DefaultTemplateLoader defaultTemplateLoader = (DefaultTemplateLoader) templateLoader;
		ResourceLoader resourceLoader = (ResourceLoader) readField(defaultTemplateLoader, "resourceLoader", true);
		assertThat(resourceLoader)
				.isNotNull()
				.isExactlyInstanceOf(CompositeResourceLoader.class);

		CompositeResourceLoader compositeResourceLoader = (CompositeResourceLoader) resourceLoader;

		@SuppressWarnings("unchecked")
		Collection<ResourceLoader> loaders = (Collection<ResourceLoader>) readField(compositeResourceLoader, "resourceLoaders", true);

		assertThat(loaders)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2);

		List<ResourceLoader> list = new ArrayList<ResourceLoader>(loaders);

		assertThat(list.get(0))
				.isNotNull()
				.isExactlyInstanceOf(ClassPathXmlApplicationContext.class);

		assertThat(list.get(1))
				.isNotNull()
				.isExactlyInstanceOf(FileSystemXmlApplicationContext.class);
	}

	@Test
	public void it_should_instantiate_mustache_compiler() {
		Mustache.Compiler compiler = mock(Mustache.Compiler.class);
		MustacheCompiler mustacheCompiler = jMustacheConfiguration.mustacheCompiler(compiler);
		assertThat(mustacheCompiler).isNotNull();
	}

	@Test
	public void it_should_create_mustache_compiler_factory_bean() throws Exception {
		JMustacheCompilerFactoryBean factoryBean = jMustacheConfiguration.jMustacheCompiler();

		String nullValue = (String) readField(factoryBean, "nullValue", true);
		String defaultValue = (String) readField(factoryBean, "defaultValue", true);
		boolean emptyStringIsFalse = (Boolean) readField(factoryBean, "emptyStringIsFalse", true);
		boolean zeroIsFalse = (Boolean) readField(factoryBean, "zeroIsFalse", true);
		boolean escapeHTML = (Boolean) readField(factoryBean, "escapeHTML", true);

		assertThat(nullValue).isEqualTo("");
		assertThat(defaultValue).isEqualTo("");
		assertThat(emptyStringIsFalse).isTrue();
		assertThat(zeroIsFalse).isTrue();
		assertThat(escapeHTML).isTrue();
	}
}
