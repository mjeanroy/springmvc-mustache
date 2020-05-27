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

package com.github.mjeanroy.springmvc.view.mustache.mustachejava;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mustachejava.MustacheResolver;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static com.github.mjeanroy.springmvc.view.mustache.tests.StringTestUtils.joinLines;
import static com.github.mjeanroy.springmvc.view.mustache.tests.TestUtils.hexIdentity;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class MustacheJavaCompilerTest {

	@Test
	public void it_should_create_compiler() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler(templateLoader);
		SpringMustacheFactory factory = readField(mustacheJavaCompiler, "mustacheFactory");

		assertThat(mustacheJavaCompiler.getPrefix()).isNull();
		assertThat(mustacheJavaCompiler.getSuffix()).isNull();
		assertThat(factory).isNotNull();
		assertThat(readField(factory, "templateLoader", MustacheTemplateLoader.class)).isSameAs(templateLoader);
	}

	@Test
	public void it_should_create_compiler_with_prefix_suffix() {
		String prefix = "/templates";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader(prefix, suffix);
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler(templateLoader);
		SpringMustacheFactory factory = readField(mustacheJavaCompiler, "mustacheFactory");

		assertThat(mustacheJavaCompiler.getPrefix()).isEqualTo(prefix);
		assertThat(mustacheJavaCompiler.getSuffix()).isEqualTo(suffix);
		assertThat(factory).isNotNull();
		assertThat(readField(factory, "templateLoader", MustacheTemplateLoader.class)).isSameAs(templateLoader);
	}

	@Test
	public void it_should_render_template() {
		String name = "/templates/foo.template.html";
		Writer writer = new StringWriter();
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler();
		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo("<div>Hello foo</div>");
	}

	@Test
	public void it_should_treat_zero_as_falsy() {
		String name = "/templates/zero.template.html";
		Writer writer = new StringWriter();
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler();
		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	",
				"	Zero should be falsy.",
				"</div>"
		)));
	}

	@Test
	public void it_should_treat_empty_string_as_falsy() {
		String name = "/templates/empty-string.template.html";
		Writer writer = new StringWriter();
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler();
		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isNotNull().isNotEmpty().isEqualTo(joinLines(asList(
				"<div>",
				"	",
				"	An empty string should be falsy.",
				"</div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial() {
		String name = "/templates/composite.template.html";
		Writer writer = new StringWriter();
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler();
		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader(prefix, suffix);
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";
		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix_event_with_full_name() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader(prefix, suffix);
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite.template.html";
		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial_aliases() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader(prefix, suffix);
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";
		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		Map<String, String> aliases = singletonMap("foo", "/templates/foo.template.html");
		templateLoader.addTemporaryPartialAliases(aliases);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		)));
	}

	@Test
	public void it_should_implement_to_string() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		MustacheJavaCompiler mustacheJavaCompiler = mustacheJavaCompiler(templateLoader);
		SpringMustacheFactory factory = readField(mustacheJavaCompiler, "mustacheFactory");

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.mustachejava.MustacheJavaCompiler@%s{" +
						"mustacheFactory=%s, " +
						"templateLoader=%s" +
				"}";
		// @formatter:on

		assertThat(mustacheJavaCompiler).hasToString(String.format(
				expectedToString, hexIdentity(mustacheJavaCompiler), factory, templateLoader
		));
	}

	@Test
	public void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(MustacheJavaCompiler.class).verify();
	}

	private static MustacheTemplateLoader mustacheTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return new DefaultTemplateLoader(resourceLoader);
	}

	private static MustacheTemplateLoader mustacheTemplateLoader(String prefix, String suffix) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return new DefaultTemplateLoader(resourceLoader, prefix, suffix);
	}

	private static MustacheJavaCompiler mustacheJavaCompiler() {
		return mustacheJavaCompiler(mustacheTemplateLoader());
	}

	private static MustacheJavaCompiler mustacheJavaCompiler(MustacheTemplateLoader templateLoader) {
		MustacheResolver mustacheResolver = new SpringMustacheResolver(templateLoader);
		SpringMustacheFactory mustacheFactory = new SpringMustacheFactory(mustacheResolver, templateLoader);
		return new MustacheJavaCompiler(mustacheFactory, templateLoader);
	}

	private static Map<String, Object> model() {
		Map<String, Object> model = new HashMap<>();
		model.put("name", "foo");
		model.put("zero", 0);
		model.put("emptyString", "");
		return model;
	}
}
