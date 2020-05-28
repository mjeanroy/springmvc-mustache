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

package com.github.mjeanroy.springmvc.view.mustache.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static com.github.mjeanroy.springmvc.view.mustache.tests.StringTestUtils.joinLines;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsCompilerTest {

	@Test
	public void it_should_build_a_compiler() {
		Handlebars handlebars = handlebars();
		HandlebarsCompiler hbCompiler = handlebarsCompiler(handlebars);

		Handlebars hb = readField(hbCompiler, "handlebars");
		MustacheTemplateLoader tmplLoader = readField(hbCompiler, "templateLoader");

		assertThat(hb).isNotNull().isSameAs(handlebars);
		assertThat(tmplLoader).isNotNull();
		assertThat(handlebars.getLoader()).isInstanceOf(HandlebarsTemplateLoader.class);
		assertThat(readField(handlebars.getLoader(), "loader", MustacheTemplateLoader.class)).isSameAs(tmplLoader);
	}

	@Test
	public void it_should_render_template() {
		Writer writer = new StringWriter();
		HandlebarsCompiler hbCompiler = handlebarsCompiler();
		String name = "/templates/foo.template.html";
		MustacheTemplate template = hbCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo("<div>Hello foo</div>");
	}

	@Test
	public void it_should_treat_zero_as_falsy() {
		Writer writer = new StringWriter();
		HandlebarsCompiler hbCompiler = handlebarsCompiler();
		String name = "/templates/zero.template.html";
		MustacheTemplate template = hbCompiler.compile(name);

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
		Writer writer = new StringWriter();
		HandlebarsCompiler hbCompiler = handlebarsCompiler();
		String name = "/templates/empty-string.template.html";
		MustacheTemplate template = hbCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	",
				"	An empty string should be falsy.",
				"</div>"
		)));
	}

	@Test
	@Ignore("With Handlebars, partials must not start with '/'")
	public void it_should_display_template_with_partial() {
		Writer writer = new StringWriter();
		HandlebarsCompiler hbCompiler = handlebarsCompiler();
		String name = "/templates/composite.template.html";
		MustacheTemplate template = hbCompiler.compile(name);

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
		HandlebarsCompiler hbCompiler = handlebarsCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";
		MustacheTemplate template = hbCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		)));
	}

	@Test
	@Ignore("With Handlebars, partials must not start with '/'")
	public void it_should_display_template_with_partial_using_prefix_suffix_even_with_full_name() {
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader(prefix, suffix);
		HandlebarsCompiler hbCompiler = handlebarsCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite.template.html";
		MustacheTemplate template = hbCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isNotNull().isNotEmpty().isEqualTo(joinLines(asList(
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
		HandlebarsCompiler hbCompiler = handlebarsCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";
		Map<String, String> aliases = Collections.singletonMap("foo", "/templates/foo.template.html");
		MustacheTemplate template = hbCompiler.compile(name);
		hbCompiler.addTemporaryPartialAliases(aliases);

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
		Handlebars handlebars = handlebars();
		HandlebarsCompiler hbCompiler = handlebarsCompiler(handlebars, templateLoader);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.handlebars.HandlebarsCompiler@%s{" +
						"handlebars=%s, " +
						"templateLoader=%s" +
				"}";
		// @formatter:on

		assertThat(hbCompiler).hasToString(String.format(
				expectedToString, hexIdentity(hbCompiler), handlebars, templateLoader
		));
	}

	@Test
	public void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HandlebarsCompiler.class)
				.withPrefabValues(Charset.class, StandardCharsets.UTF_8, StandardCharsets.UTF_16)
				.verify();
	}

	private static MustacheTemplateLoader mustacheTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return new DefaultTemplateLoader(resourceLoader);
	}

	private static MustacheTemplateLoader mustacheTemplateLoader(String prefix, String suffix) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return new DefaultTemplateLoader(resourceLoader, prefix, suffix);
	}

	private static Handlebars handlebars() {
		return new Handlebars();
	}

	private static HandlebarsCompiler handlebarsCompiler() {
		return handlebarsCompiler(handlebars(), mustacheTemplateLoader());
	}

	private static HandlebarsCompiler handlebarsCompiler(Handlebars handlebars) {
		return handlebarsCompiler(handlebars, mustacheTemplateLoader());
	}

	private static HandlebarsCompiler handlebarsCompiler(MustacheTemplateLoader templateLoader) {
		return handlebarsCompiler(handlebars(), templateLoader);
	}

	private static HandlebarsCompiler handlebarsCompiler(Handlebars handlebars, MustacheTemplateLoader templateLoader) {
		return new HandlebarsCompiler(handlebars, templateLoader);
	}

	private static Map<String, Object> model() {
		Map<String, Object> model = new HashMap<>();
		model.put("name", "foo");
		model.put("zero", 0);
		model.put("emptyString", "");
		return model;
	}
}
