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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.CompositeResourceLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.apache.commons.lang3.reflect.FieldUtils.readStaticField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MustacheTemplateLoaderFactoryBeanTest {

	private MustacheTemplateLoaderFactoryBean factoryBean;

	private ResourceLoader classpathResourceLoader;

	@Before
	public void setUp() throws Exception {
		factoryBean = new MustacheTemplateLoaderFactoryBean();
		classpathResourceLoader = (ResourceLoader) readStaticField(MustacheTemplateLoaderFactoryBean.class, "CLASSPATH_RESOURCE_LOADER", true);
	}

	@Test
	public void it_should_be_a_singleton() {
		assertThat(factoryBean.isSingleton()).isTrue();
	}

	@Test
	public void it_should_have_object_type() {
		assertThat(factoryBean.getObjectType()).isEqualTo(MustacheTemplateLoader.class);
	}

	@Test
	public void it_should_create_template_loader_with_unique_loaders() throws Exception {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		factoryBean.setApplicationContext(applicationContext);
		factoryBean.setResourceLoader(applicationContext);
		factoryBean.afterPropertiesSet();

		MustacheTemplateLoader templateLoader = factoryBean.getObject();

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
				.hasSize(2)
				.containsOnly(
						applicationContext,
						classpathResourceLoader
				);
	}

	@Test
	public void it_should_instantiate_template_loader_using_given_loaders() throws Exception {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		ResourceLoader resourceLoader = mock(ResourceLoader.class);

		factoryBean.setApplicationContext(applicationContext);
		factoryBean.setResourceLoader(resourceLoader);
		factoryBean.afterPropertiesSet();

		MustacheTemplateLoader templateLoader = factoryBean.getObject();

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
				.hasSize(3)
				.containsOnly(
						applicationContext,
						resourceLoader,
						classpathResourceLoader
				);
	}

	@Test
	public void it_should_instantiate_template_loader_using_default_loaders() throws Exception {
		factoryBean.setApplicationContext(null);
		factoryBean.setResourceLoader(null);
		factoryBean.afterPropertiesSet();

		MustacheTemplateLoader templateLoader = factoryBean.getObject();

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
				.hasSize(3);

		List<ResourceLoader> list = new ArrayList<ResourceLoader>(loaders);

		assertThat(list.get(0))
				.isNotNull()
				.isExactlyInstanceOf(ClassPathXmlApplicationContext.class);

		assertThat(list.get(1))
				.isNotNull()
				.isExactlyInstanceOf(FileSystemXmlApplicationContext.class);

		assertThat(list.get(2))
				.isNotNull()
				.isSameAs(classpathResourceLoader);
	}

	@Test
	public void classpath_resource_loader_should_load_resource_from_classpath() throws Exception {
		Resource resource = classpathResourceLoader.getResource("/templates/foo.template.html");
		assertThat(resource).isNotNull();
		assertThat(resource.exists()).isTrue();
		assertThat(resource.getURI().toString())
				.startsWith("file:/")
				.endsWith("/target/test-classes/templates/foo.template.html");
	}

	@Test
	public void classpath_resource_loader_should_load_resource_from_classpath_and_remove_prefix() throws Exception {
		Resource resource = classpathResourceLoader.getResource("classpath:/templates/foo.template.html");
		assertThat(resource).isNotNull();
		assertThat(resource.exists()).isTrue();
		assertThat(resource.getURI().toString())
				.startsWith("file:/")
				.endsWith("/target/test-classes/templates/foo.template.html");
	}

	@Test
	public void classpath_resource_loader_should_load_resource_from_classpath_and_return_resource_if_it_does_not_exist() throws Exception {
		Resource resource = classpathResourceLoader.getResource("classpath:/templates/fake.template.html");
		assertThat(resource).isNotNull();
		assertThat(resource.exists()).isFalse();
	}
}
