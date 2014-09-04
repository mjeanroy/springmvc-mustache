/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheTemplateNotFoundException;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class JMustacheTemplateLoaderTest {

	@Rule
	public ExpectedException thrown = none();

	@Mock
	private Resource resource;

	@Mock
	private ResourceLoader resourceLoader;

	private String prefix;

	private String suffix;

	private JMustacheTemplateLoader mustacheTemplateLoader;

	@Before
	public void setUp() {
		prefix = "foo";
		suffix = "bar";
		mustacheTemplateLoader = new JMustacheTemplateLoader(resourceLoader);
	}

	@Test
	public void it_should_build_template_loader_using_default_resource_loader() throws Exception {
		JMustacheTemplateLoader loader = new JMustacheTemplateLoader();

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(loader, "prefix", true);
		String suffix = (String) readField(loader, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		assertThat(resourceLoader)
				.isNotNull()
				.isExactlyInstanceOf(DefaultResourceLoader.class);

		assertThat(prefix).isNull();
		assertThat(suffix).isNull();
		assertThat(partialsAliases)
				.isNotNull()
				.isEmpty();
	}

	@Test
	public void it_should_build_template_loader_using_custom_resource_loader() throws Exception {
		JMustacheTemplateLoader loader = new JMustacheTemplateLoader(resourceLoader);

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(loader, "prefix", true);
		String suffix = (String) readField(loader, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		assertThat(resourceLoader)
				.isNotNull()
				.isSameAs(this.resourceLoader);

		assertThat(prefix).isNull();
		assertThat(suffix).isNull();
		assertThat(partialsAliases)
				.isNotNull()
				.isEmpty();
	}

	@Test
	public void it_should_build_template_loader_using_custom_resource_loader_with_prefix_and_suffix() throws Exception {
		JMustacheTemplateLoader loader = new JMustacheTemplateLoader(resourceLoader, prefix, suffix);

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(loader, "prefix", true);
		String suffix = (String) readField(loader, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		assertThat(resourceLoader)
				.isNotNull()
				.isSameAs(resourceLoader);

		assertThat(prefix)
				.isNotNull()
				.isEqualTo(this.prefix);

		assertThat(suffix)
				.isNotNull()
				.isEqualTo(this.suffix);
		assertThat(partialsAliases)
				.isNotNull()
				.isEmpty();
	}

	@Test
	public void it_should_add_partial_aliases() throws Exception {
		String k1 = "foo";
		String v1 = "bar";
		String k2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		JMustacheTemplateLoader loader = new JMustacheTemplateLoader(resourceLoader, prefix, suffix);
		loader.addPartialAliases(aliases);

		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		assertThat(partialsAliases)
				.isNotNull()
				.isNotEmpty()
				.hasSize(aliases.size())
				.containsOnly(
						entry(k1, v1),
						entry(k2, v2)
				);
	}

	@Test
	public void it_should_clone_template_loader() throws Exception {
		String k1 = "foo";
		String v1 = "bar";
		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);

		JMustacheTemplateLoader loader = new JMustacheTemplateLoader(resourceLoader, prefix, suffix);
		loader.addPartialAliases(aliases);
		Map<String, String> originalPartialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		JMustacheTemplateLoader clone = loader.clone();

		assertThat(clone).isNotSameAs(loader);

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(clone, "prefix", true);
		String suffix = (String) readField(clone, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(clone, "partialAliases", true);

		assertThat(resourceLoader)
				.isNotNull()
				.isSameAs(this.resourceLoader);

		assertThat(prefix)
				.isNotNull()
				.isEqualTo(this.prefix);

		assertThat(suffix)
				.isNotNull()
				.isEqualTo(this.suffix);

		assertThat(partialsAliases)
				.isNotNull()
				.isNotSameAs(originalPartialsAliases)
				.isEqualTo(originalPartialsAliases);
	}

	@Test
	public void it_should_clone_template_loader_without_prefix_suffix() throws Exception {
		JMustacheTemplateLoader loader = new JMustacheTemplateLoader(resourceLoader);
		JMustacheTemplateLoader clone = loader.clone();

		assertThat(clone).isNotSameAs(loader);

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(clone, "prefix", true);
		String suffix = (String) readField(clone, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(clone, "partialAliases", true);

		assertThat(resourceLoader)
				.isNotNull()
				.isSameAs(this.resourceLoader);

		assertThat(prefix).isNull();
		assertThat(suffix).isNull();

		assertThat(partialsAliases)
				.isNotNull()
				.isEmpty();
	}

	@Test
	public void it_should_throw_exception_when_resource_does_not_exist() throws Exception {
		String name = "/templates/does_not_exist.template.html";
		when(resource.exists()).thenReturn(false);
		when(resourceLoader.getResource(name)).thenReturn(resource);

		thrown.expect(MustacheTemplateNotFoundException.class);
		thrown.expectMessage("Mustache template /templates/does_not_exist.template.html does not exist");

		mustacheTemplateLoader.getTemplate(name);
	}

	@Test
	public void it_should_return_reader_when_resource_exist() throws Exception {
		String name = "/templates/foo.template.html";
		InputStream is = getClass().getResourceAsStream(name);

		when(resource.exists()).thenReturn(true);
		when(resource.getInputStream()).thenReturn(is);
		when(resourceLoader.getResource(name)).thenReturn(resource);

		Reader reader = mustacheTemplateLoader.getTemplate(name);

		assertThat(reader)
				.isNotNull();
	}

	@Test
	public void it_should_read_template_using_prefix_and_suffix()
			throws Exception {
		String name = "foo";
		String prefix = "/";
		String suffix = ".template.html";
		String templateName = prefix + name + suffix;
		JMustacheTemplateLoader mustacheTemplateLoader = new JMustacheTemplateLoader(resourceLoader, prefix, suffix);

		when(resource.exists()).thenReturn(false);
		when(resourceLoader.getResource(templateName)).thenReturn(resource);

		thrown.expect(MustacheTemplateNotFoundException.class);
		thrown.expectMessage("Mustache template /foo.template.html does not exist");

		Reader reader = mustacheTemplateLoader.getTemplate(name);

		assertThat(reader)
				.isNotNull();

		verify(resourceLoader).getResource(templateName);
		verify(resourceLoader, never()).getResource(name);
	}
}
