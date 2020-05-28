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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.ReflectionTestUtils.readField;
import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.ReflectionTestUtils.readStaticField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MustacheTemplateLoaderFactoryBeanTest {

	private ResourceLoader classpathResourceLoader;

	@BeforeEach
	void setUp() {
		classpathResourceLoader = readStaticField(MustacheTemplateLoaderFactoryBean.class, "CLASSPATH_RESOURCE_LOADER");
	}

	@Test
	void it_should_be_a_singleton() {
		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		assertThat(factoryBean.isSingleton()).isTrue();
	}

	@Test
	void it_should_have_object_type() {
		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		assertThat(factoryBean.getObjectType()).isEqualTo(MustacheTemplateLoader.class);
	}

	@Test
	void it_should_create_template_loader_with_unique_loaders() throws Exception {
		ApplicationContext applicationContext = mock(ApplicationContext.class);

		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setApplicationContext(applicationContext);
		factoryBean.setResourceLoader(applicationContext);
		factoryBean.afterPropertiesSet();

		MustacheTemplateLoader templateLoader = factoryBean.getObject();
		assertThat(templateLoader).isExactlyInstanceOf(DefaultTemplateLoader.class);

		DefaultTemplateLoader defaultTemplateLoader = (DefaultTemplateLoader) templateLoader;
		ResourceLoader resourceLoader = readField(defaultTemplateLoader, "resourceLoader");
		assertThat(resourceLoader).isExactlyInstanceOf(CompositeResourceLoader.class);

		CompositeResourceLoader compositeResourceLoader = (CompositeResourceLoader) resourceLoader;
		Collection<ResourceLoader> loaders = readField(compositeResourceLoader, "resourceLoaders");

		assertThat(loaders).hasSize(2).containsOnly(
				applicationContext,
				classpathResourceLoader
		);
	}

	@Test
	void it_should_instantiate_template_loader_using_given_loaders() throws Exception {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		ResourceLoader resourceLoader = new DefaultResourceLoader();

		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setApplicationContext(applicationContext);
		factoryBean.setResourceLoader(resourceLoader);
		factoryBean.afterPropertiesSet();

		MustacheTemplateLoader templateLoader = factoryBean.getObject();
		assertThat(templateLoader).isExactlyInstanceOf(DefaultTemplateLoader.class);

		DefaultTemplateLoader defaultTemplateLoader = (DefaultTemplateLoader) templateLoader;
		ResourceLoader rl = readField(defaultTemplateLoader, "resourceLoader");
		assertThat(rl).isExactlyInstanceOf(CompositeResourceLoader.class);

		CompositeResourceLoader compositeResourceLoader = (CompositeResourceLoader) rl;
		Collection<ResourceLoader> loaders = readField(compositeResourceLoader, "resourceLoaders");

		assertThat(loaders).hasSize(3).containsOnly(
				applicationContext,
				resourceLoader,
				classpathResourceLoader
		);
	}

	@Test
	void it_should_instantiate_template_loader_using_default_loaders() throws Exception {
		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setApplicationContext(null);
		factoryBean.setResourceLoader(null);
		factoryBean.afterPropertiesSet();

		MustacheTemplateLoader templateLoader = factoryBean.getObject();
		assertThat(templateLoader).isExactlyInstanceOf(DefaultTemplateLoader.class);

		DefaultTemplateLoader defaultTemplateLoader = (DefaultTemplateLoader) templateLoader;
		ResourceLoader resourceLoader = readField(defaultTemplateLoader, "resourceLoader");
		assertThat(resourceLoader).isExactlyInstanceOf(CompositeResourceLoader.class);

		CompositeResourceLoader compositeResourceLoader = (CompositeResourceLoader) resourceLoader;
		Collection<ResourceLoader> loaders = readField(compositeResourceLoader, "resourceLoaders");
		assertThat(loaders).hasSize(3);

		List<ResourceLoader> list = new ArrayList<>(loaders);
		assertThat(list.get(0)).isExactlyInstanceOf(ClassPathXmlApplicationContext.class);
		assertThat(list.get(1)).isExactlyInstanceOf(FileSystemXmlApplicationContext.class);
		assertThat(list.get(2)).isSameAs(classpathResourceLoader);
	}

	@Test
	void it_should_set_prefix() {
		String prefix = "/templates/";

		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setPrefix(prefix);

		MustacheTemplateLoader templateLoader = factoryBean.createInstance();
		assertThat(templateLoader.getPrefix()).isEqualTo(prefix);
	}

	@Test
	void it_should_set_suffix() {
		String suffix = ".template.html";

		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setSuffix(suffix);

		MustacheTemplateLoader templateLoader = factoryBean.createInstance();
		assertThat(templateLoader.getSuffix()).isEqualTo(suffix);
	}

	@Test
	@SuppressWarnings("unchecked")
	void it_should_set_partial_aliases() {
		Map<String, String> partialAliases = Collections.singletonMap(
				"john", "jane"
		);

		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setPartialAliases(partialAliases);

		MustacheTemplateLoader templateLoader = factoryBean.createInstance();
		assertThat(readField(templateLoader, "partialAliases", Map.class)).isNotSameAs(partialAliases).isEqualTo(partialAliases);
	}

	@Test
	void classpath_resource_loader_should_load_resource_from_classpath() throws Exception {
		Resource resource = classpathResourceLoader.getResource("/templates/foo.template.html");
		assertThat(resource).isNotNull();
		assertThat(resource.exists()).isTrue();
		assertThat(resource.getURI().toString())
				.startsWith("file:/")
				.endsWith("/target/test-classes/templates/foo.template.html");
	}

	@Test
	void classpath_resource_loader_should_load_resource_from_classpath_and_remove_prefix() throws Exception {
		Resource resource = classpathResourceLoader.getResource("classpath:/templates/foo.template.html");
		assertThat(resource).isNotNull();
		assertThat(resource.exists()).isTrue();
		assertThat(resource.getURI().toString())
				.startsWith("file:/")
				.endsWith("/target/test-classes/templates/foo.template.html");
	}

	@Test
	void classpath_resource_loader_should_load_resource_from_classpath_and_return_resource_if_it_does_not_exist() {
		Resource resource = classpathResourceLoader.getResource("classpath:/templates/fake.template.html");
		assertThat(resource).isNotNull();
		assertThat(resource.exists()).isFalse();
	}

	@Test
	void it_should_implement_to_string() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		ApplicationContext applicationContext = mock(ApplicationContext.class);

		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setPrefix("/templates");
		factoryBean.setSuffix(".template.html");
		factoryBean.setResourceLoader(resourceLoader);
		factoryBean.setApplicationContext(applicationContext);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheTemplateLoaderFactoryBean@%s{" +
						"resourceLoader=%s, " +
						"prefix=\"/templates\", " +
						"suffix=\".template.html\", " +
						"applicationContext=%s" +
				"}";
		// @formatter:on

		assertThat(factoryBean).hasToString(String.format(
				expectedToString, hexIdentity(factoryBean), resourceLoader, applicationContext
		));
	}
}
