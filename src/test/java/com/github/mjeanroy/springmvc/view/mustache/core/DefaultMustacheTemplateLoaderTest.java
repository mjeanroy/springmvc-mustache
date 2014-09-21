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

package com.github.mjeanroy.springmvc.view.mustache.core;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheTemplateNotFoundException;
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

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class DefaultMustacheTemplateLoaderTest {

	@Rule
	public final ExpectedException thrown = none();

	@Mock
	private Resource resource;

	@Mock
	private ResourceLoader resourceLoader;

	private String prefix;

	private String suffix;

	private DefaultMustacheTemplateLoader mustacheTemplateLoader;

	@Before
	public void setUp() {
		prefix = "foo";
		suffix = "bar";
		mustacheTemplateLoader = new DefaultMustacheTemplateLoader(resourceLoader);
	}

	@Test
	public void it_should_build_template_loader_using_default_resource_loader() throws Exception {
		DefaultMustacheTemplateLoader loader = new DefaultMustacheTemplateLoader();

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(loader, "prefix", true);
		String suffix = (String) readField(loader, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		assertThat(resourceLoader).isNotNull().isExactlyInstanceOf(DefaultResourceLoader.class);
		assertThat(prefix).isNull();
		assertThat(suffix).isNull();
		assertThat(partialsAliases).isNotNull().isEmpty();
	}

	@Test
	public void it_should_build_template_loader_using_custom_resource_loader() throws Exception {
		DefaultMustacheTemplateLoader loader = new DefaultMustacheTemplateLoader(resourceLoader);

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(loader, "prefix", true);
		String suffix = (String) readField(loader, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		assertThat(resourceLoader).isNotNull().isSameAs(this.resourceLoader);
		assertThat(prefix).isNull();
		assertThat(suffix).isNull();
		assertThat(partialsAliases).isNotNull().isEmpty();
	}

	@Test
	public void it_should_build_template_loader_using_custom_resource_loader_with_prefix_and_suffix() throws Exception {
		DefaultMustacheTemplateLoader loader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);

		ResourceLoader resourceLoader = (ResourceLoader) readField(loader, "resourceLoader", true);
		String prefix = (String) readField(loader, "prefix", true);
		String suffix = (String) readField(loader, "suffix", true);
		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);

		assertThat(resourceLoader).isNotNull().isSameAs(resourceLoader);
		assertThat(prefix).isNotNull().isEqualTo(this.prefix);
		assertThat(suffix).isNotNull().isEqualTo(this.suffix);
		assertThat(partialsAliases).isNotNull().isEmpty();
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

		DefaultMustacheTemplateLoader loader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);
		loader.addPartialAliases(aliases);

		Map<String, String> partialsAliases = (Map<String, String>) readField(loader, "partialAliases", true);
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(aliases.size()).containsOnly(
				entry(k1, v1),
				entry(k2, v2)
		);
	}

	@Test
	public void it_should_add_temporary_partial_aliases() throws Exception {
		String k1 = "foo";
		String v1 = "bar";
		String k2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		DefaultMustacheTemplateLoader loader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);
		loader.addTemporaryPartialAliases(aliases);

		ThreadLocal<Map<String, String>> tl = (ThreadLocal<Map<String, String>>) readField(loader, "temporaryPartialAliases", true);
		Map<String, String> partialsAliases = tl.get();
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(aliases.size()).containsOnly(
				entry(k1, v1),
				entry(k2, v2)
		);
	}

	@Test
	public void it_should_remove_temporary_partial_aliases() throws Exception {
		String k1 = "foo";
		String v1 = "bar";
		String k2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		DefaultMustacheTemplateLoader loader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);
		loader.addTemporaryPartialAliases(aliases);

		ThreadLocal<Map<String, String>> tl = (ThreadLocal<Map<String, String>>) readField(loader, "temporaryPartialAliases", true);
		Map<String, String> partialsAliases = tl.get();
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(aliases.size()).containsOnly(
				entry(k1, v1),
				entry(k2, v2)
		);

		loader.removeTemporaryPartialAliases();

		tl = (ThreadLocal<Map<String, String>>) readField(loader, "temporaryPartialAliases", true);
		partialsAliases = tl.get();
		assertThat(partialsAliases).isNotNull().isEmpty();
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

		assertThat(reader).isNotNull();
	}

	@Test
	public void it_should_read_template_using_prefix_and_suffix() throws Exception {
		String name = "foo";
		String prefix = "/";
		String suffix = ".template.html";
		String templateName = prefix + name + suffix;
		DefaultMustacheTemplateLoader mustacheTemplateLoader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);

		when(resource.exists()).thenReturn(false);
		when(resourceLoader.getResource(templateName)).thenReturn(resource);

		thrown.expect(MustacheTemplateNotFoundException.class);
		thrown.expectMessage("Mustache template /foo.template.html does not exist");

		Reader reader = mustacheTemplateLoader.getTemplate(name);

		assertThat(reader).isNotNull();
		verify(resourceLoader).getResource(templateName);
		verify(resourceLoader, never()).getResource(name);
	}

	@Test
	public void it_should_get_and_set_prefix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		DefaultMustacheTemplateLoader mustacheTemplateLoader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);
		assertThat(mustacheTemplateLoader.getPrefix()).isNotNull().isNotEmpty().isEqualTo(prefix);

		String newPrefix = "foobar";
		mustacheTemplateLoader.setPrefix(newPrefix);
		assertThat(mustacheTemplateLoader.getPrefix()).isNotNull().isNotEmpty().isEqualTo(newPrefix);
	}

	@Test
	public void it_should_get_and_set_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		DefaultMustacheTemplateLoader mustacheTemplateLoader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);
		assertThat(mustacheTemplateLoader.getSuffix()).isNotNull().isNotEmpty().isEqualTo(suffix);

		String newSuffix = "foobar";
		mustacheTemplateLoader.setSuffix(newSuffix);
		assertThat(mustacheTemplateLoader.getSuffix()).isNotNull().isNotEmpty().isEqualTo(newSuffix);
	}

	@Test
	public void it_should_resolve_template_location_without_prefix_suffix() {
		String templateName = "foo";
		String location = mustacheTemplateLoader.resolve(templateName);
		assertThat(location).isNotNull().isNotEmpty().isEqualTo(templateName);
	}

	@Test
	public void it_should_resolve_template_location_with_prefix_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		String templateName = "foo";
		DefaultMustacheTemplateLoader mustacheTemplateLoader = new DefaultMustacheTemplateLoader(resourceLoader, prefix, suffix);

		String location = mustacheTemplateLoader.resolve(templateName);

		assertThat(location).isNotNull().isNotEmpty().isEqualTo(prefix + templateName + suffix);
	}
}
