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

package com.github.mjeanroy.springmvc.view.mustache.core;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheTemplateNotFoundException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultTemplateLoaderTest {

	private ResourceLoader resourceLoader;
	private String prefix;
	private String suffix;

	private DefaultTemplateLoader mustacheTemplateLoader;

	@Before
	public void setUp() {
		prefix = "foo";
		suffix = "bar";
		resourceLoader = mock(ResourceLoader.class);
		mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
	}

	@Test
	public void it_should_build_template_loader_using_custom_resource_loader() {
		DefaultTemplateLoader loader = new DefaultTemplateLoader(resourceLoader);

		ResourceLoader resourceLoader = readField(loader, "resourceLoader");
		String prefix = readField(loader, "prefix");
		String suffix = readField(loader, "suffix");
		Map<String, String> partialsAliases = readField(loader, "partialAliases");

		assertThat(resourceLoader).isNotNull().isSameAs(this.resourceLoader);
		assertThat(prefix).isNull();
		assertThat(suffix).isNull();
		assertThat(partialsAliases).isNotNull().isEmpty();
	}

	@Test
	public void it_should_build_template_loader_using_custom_resource_loader_with_prefix_and_suffix() {
		DefaultTemplateLoader loader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);

		ResourceLoader resourceLoader = readField(loader, "resourceLoader");
		String prefix = readField(loader, "prefix");
		String suffix = readField(loader, "suffix");
		Map<String, String> partialsAliases = readField(loader, "partialAliases");

		assertThat(resourceLoader).isNotNull().isSameAs(resourceLoader);
		assertThat(prefix).isNotNull().isEqualTo(this.prefix);
		assertThat(suffix).isNotNull().isEqualTo(this.suffix);
		assertThat(partialsAliases).isNotNull().isEmpty();
	}

	@Test
	public void it_should_add_partial_aliases() {
		String k1 = "foo";
		String v1 = "bar";
		String k2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		DefaultTemplateLoader loader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		loader.addPartialAliases(aliases);

		Map<String, String> partialsAliases = readField(loader, "partialAliases");
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(aliases.size()).containsOnly(
				entry(k1, v1),
				entry(k2, v2)
		);
	}

	@Test
	public void it_should_add_temporary_partial_aliases() {
		String k1 = "foo";
		String v1 = "bar";
		String k2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		DefaultTemplateLoader loader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		loader.addTemporaryPartialAliases(aliases);

		ThreadLocal<Map<String, String>> tl = readField(loader, "temporaryPartialAliases");
		Map<String, String> partialsAliases = tl.get();
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(aliases.size()).containsOnly(
				entry(k1, v1),
				entry(k2, v2)
		);
	}

	@Test
	public void it_should_remove_temporary_partial_aliases() {
		String k1 = "foo";
		String v1 = "bar";
		String k2 = "bar";
		String v2 = "foo";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		DefaultTemplateLoader loader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		loader.addTemporaryPartialAliases(aliases);

		ThreadLocal<Map<String, String>> tl = readField(loader, "temporaryPartialAliases");
		Map<String, String> partialsAliases = tl.get();
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(aliases.size()).containsOnly(
				entry(k1, v1),
				entry(k2, v2)
		);

		loader.removeTemporaryPartialAliases();

		tl = readField(loader, "temporaryPartialAliases");
		partialsAliases = tl.get();
		assertThat(partialsAliases).isNotNull().isEmpty();
	}

	@Test
	public void it_should_throw_exception_when_resource_does_not_exist() {
		final String name = "/templates/does_not_exist.template.html";
		final Resource resource = mock(Resource.class);

		when(resource.exists()).thenReturn(false);
		when(resourceLoader.getResource(name)).thenReturn(resource);

		final ThrowingCallable getTemplate = new ThrowingCallable() {
			@Override
			public void call() {
				mustacheTemplateLoader.getTemplate(name);
			}
		};

		assertThatThrownBy(getTemplate)
				.isInstanceOf(MustacheTemplateNotFoundException.class)
				.hasMessage("Mustache template /templates/does_not_exist.template.html does not exist");
	}

	@Test
	public void it_should_return_reader_when_resource_exist() throws Exception {
		String name = "/templates/foo.template.html";
		InputStream is = getClass().getResourceAsStream(name);

		Resource resource = mock(Resource.class);
		when(resource.exists()).thenReturn(true);
		when(resource.getInputStream()).thenReturn(is);
		when(resourceLoader.getResource(name)).thenReturn(resource);

		Reader reader = mustacheTemplateLoader.getTemplate(name);

		assertThat(reader).isNotNull();
	}

	@Test
	public void it_should_read_template_using_prefix_and_suffix() {
		final String name = "foo";
		final String prefix = "/";
		final String suffix = ".template.html";
		final String templateName = prefix + name + suffix;
		final DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		final Resource resource = mock(Resource.class);

		when(resource.exists()).thenReturn(false);
		when(resourceLoader.getResource(templateName)).thenReturn(resource);

		final ThrowingCallable getTemplate = new ThrowingCallable() {
			@Override
			public void call() {
				mustacheTemplateLoader.getTemplate(name);
			}
		};

		assertThatThrownBy(getTemplate)
				.isInstanceOf(MustacheTemplateNotFoundException.class)
				.hasMessage("Mustache template /foo.template.html does not exist");

		verify(resourceLoader, never()).getResource(name);
	}

	@Test
	public void it_should_get_and_set_prefix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		assertThat(mustacheTemplateLoader.getPrefix()).isNotNull().isNotEmpty().isEqualTo(prefix);

		String newPrefix = "foobar";
		mustacheTemplateLoader.setPrefix(newPrefix);
		assertThat(mustacheTemplateLoader.getPrefix()).isNotNull().isNotEmpty().isEqualTo(newPrefix);
	}

	@Test
	public void it_should_get_and_set_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
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
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);

		String location = mustacheTemplateLoader.resolve(templateName);

		assertThat(location).isNotNull().isNotEmpty().isEqualTo(prefix + templateName + suffix);
	}

	@Test
	public void it_should_resolve_template_location_with_prefix_suffix_and_aliases() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		String templateName = "foo";
		String realName = "bar";
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		mustacheTemplateLoader.addPartialAliases(singletonMap(templateName, realName));

		String location = mustacheTemplateLoader.resolve(templateName);

		assertThat(location).isNotNull().isNotEmpty().isEqualTo(prefix + realName + suffix);
	}
}
