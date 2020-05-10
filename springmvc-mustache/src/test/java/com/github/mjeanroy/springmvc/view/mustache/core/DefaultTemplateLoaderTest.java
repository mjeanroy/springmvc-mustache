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
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.IOTestUtils.read;
import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;

public class DefaultTemplateLoaderTest {

	@Test
	@SuppressWarnings("unchecked")
	public void it_should_build_template_loader_using_custom_resource_loader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader loader = new DefaultTemplateLoader(resourceLoader);

		assertThat(readField(loader, "resourceLoader")).isSameAs(resourceLoader);
		assertThat(readField(loader, "prefix")).isNull();
		assertThat(readField(loader, "suffix")).isNull();
		assertThat((Map<String, String>) readField(loader, "partialAliases")).isNotNull().isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void it_should_build_template_loader_using_custom_resource_loader_with_prefix_and_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader loader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);

		assertThat(readField(loader, "resourceLoader")).isSameAs(resourceLoader);
		assertThat(readField(loader, "prefix")).isEqualTo(prefix);
		assertThat(readField(loader, "suffix")).isEqualTo(suffix);
		assertThat((Map<String, String>) readField(loader, "partialAliases")).isNotNull().isEmpty();
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

		DefaultTemplateLoader loader = defaultTemplateLoader();
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

		DefaultTemplateLoader loader = defaultTemplateLoader();
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

		DefaultTemplateLoader loader = defaultTemplateLoader();
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
		final DefaultTemplateLoader loader = defaultTemplateLoader();
		final ThrowingCallable getTemplate = new ThrowingCallable() {
			@Override
			public void call() {
				loader.getTemplate(name);
			}
		};

		assertThatThrownBy(getTemplate)
				.isInstanceOf(MustacheTemplateNotFoundException.class)
				.hasMessage("Mustache template /templates/does_not_exist.template.html does not exist");
	}

	@Test
	public void it_should_return_reader_when_resource_exist() {
		String name = "/templates/foo.template.html";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);

		Reader reader = mustacheTemplateLoader.getTemplate(name);

		assertThat(reader).isNotNull();
		assertThat(read(reader)).isEqualTo("<div>Hello {{name}}</div>");
	}

	@Test
	public void it_should_return_reader_when_resource_exist_using_prefix_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		String name = "foo";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);

		Reader reader = mustacheTemplateLoader.getTemplate(name);

		assertThat(reader).isNotNull();
		assertThat(read(reader)).isEqualTo("<div>Hello {{name}}</div>");
	}

	@Test
	public void it_should_read_template_using_prefix_and_suffix() {
		final String name = "foo";
		final String prefix = "/";
		final String suffix = ".template.html";
		final ResourceLoader resourceLoader = new DefaultResourceLoader();
		final DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);

		final ThrowingCallable getTemplate = new ThrowingCallable() {
			@Override
			public void call() {
				mustacheTemplateLoader.getTemplate(name);
			}
		};

		assertThatThrownBy(getTemplate)
				.isInstanceOf(MustacheTemplateNotFoundException.class)
				.hasMessage("Mustache template /foo.template.html does not exist");
	}

	@Test
	public void it_should_get_and_set_prefix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		assertThat(mustacheTemplateLoader.getPrefix()).isEqualTo(prefix);

		String newPrefix = "foobar";
		mustacheTemplateLoader.setPrefix(newPrefix);
		assertThat(mustacheTemplateLoader.getPrefix()).isEqualTo(newPrefix);
	}

	@Test
	public void it_should_get_and_set_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		assertThat(mustacheTemplateLoader.getSuffix()).isEqualTo(suffix);

		String newSuffix = "foobar";
		mustacheTemplateLoader.setSuffix(newSuffix);
		assertThat(mustacheTemplateLoader.getSuffix()).isEqualTo(newSuffix);
	}

	@Test
	public void it_should_resolve_template_location_without_prefix_suffix() {
		String templateName = "foo";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		String location = mustacheTemplateLoader.resolve(templateName);
		assertThat(location).isEqualTo(templateName);
	}

	@Test
	public void it_should_resolve_template_location_with_prefix_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		String templateName = "foo";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);

		String location = mustacheTemplateLoader.resolve(templateName);

		assertThat(location).isEqualTo(prefix + templateName + suffix);
	}

	@Test
	public void it_should_resolve_template_location_with_prefix_suffix_and_aliases() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		String templateName = "foo";
		String realName = "bar";
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		DefaultTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		mustacheTemplateLoader.addPartialAliases(singletonMap(templateName, realName));

		String location = mustacheTemplateLoader.resolve(templateName);

		assertThat(location).isEqualTo(prefix + realName + suffix);
	}

	@Test
	public void it_should_implement_to_string() {
		ResourceLoader resourceLoader = mock(ResourceLoader.class, "ResourceLoader");
		DefaultTemplateLoader templateLoader = defaultTemplateLoader(resourceLoader);
		String identity = hexIdentity(templateLoader);

		// @formatter:off
		assertThat(templateLoader).hasToString(
				"com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader@" + identity + "{" +
						"resourceLoader=ResourceLoader, " +
						"prefix=\"/templates/\", " +
						"suffix=\".template.html\", " +
						"partialAliases={}, " +
						"temporaryPartialAliases={}" +
				"}"
		);
		// @formatter:on
	}

	private static DefaultTemplateLoader defaultTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return defaultTemplateLoader(resourceLoader);
	}

	private static DefaultTemplateLoader defaultTemplateLoader(ResourceLoader resourceLoader) {
		String prefix = "/templates/";
		String suffix = ".template.html";
		String templateName = "foo";
		return new DefaultTemplateLoader(resourceLoader, prefix, suffix);
	}
}
