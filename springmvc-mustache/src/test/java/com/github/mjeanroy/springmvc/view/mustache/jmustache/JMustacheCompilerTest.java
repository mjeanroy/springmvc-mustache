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

package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.samskivert.mustache.Mustache;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.StringTestUtils.joinLines;
import static com.samskivert.mustache.Mustache.Compiler;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class JMustacheCompilerTest {

	@Test
	public void it_should_render_template() {
		Writer writer = new StringWriter();
		String name = "/templates/foo.template.html";
		JMustacheCompiler mustacheCompiler = jMustacheCompiler();
		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		String expected = "<div>Hello foo</div>";
		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_zero_as_falsy() {
		Writer writer = new StringWriter();
		String name = "/templates/zero.template.html";
		JMustacheCompiler mustacheCompiler = jMustacheCompiler();
		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		String expected = joinLines(asList(
				"<div>",
				"	",
				"	Zero should be falsy.",
				"</div>"
		));

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_empty_string_as_falsy() {
		Writer writer = new StringWriter();
		String name = "/templates/empty-string.template.html";
		JMustacheCompiler mustacheCompiler = jMustacheCompiler();
		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		String expected = joinLines(asList(
				"<div>",
				"	",
				"	An empty string should be falsy.",
				"</div>"
		));

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial() {
		Writer writer = new StringWriter();
		String name = "/templates/composite.template.html";
		JMustacheCompiler mustacheCompiler = jMustacheCompiler();
		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix() {
		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";

		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");

		JMustacheCompiler mustacheCompiler = jMustacheCompiler(templateLoader);
		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix_event_with_full_name() {
		Writer writer = new StringWriter();
		String name = "/templates/composite.template.html";

		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");

		JMustacheCompiler mustacheCompiler = jMustacheCompiler(templateLoader);
		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_aliases() {
		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";
		Map<String, String> aliases = Collections.singletonMap(
				"foo", "/templates/foo.template.html"
		);

		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		templateLoader.addTemporaryPartialAliases(aliases);

		JMustacheCompiler mustacheCompiler = jMustacheCompiler(templateLoader);
		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		templateLoader.removeTemporaryPartialAliases();

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_implement_to_string() {
		Compiler compiler = compiler();
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		MustacheCompiler mustacheCompiler = jMustacheCompiler(compiler, templateLoader);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler@%s{" +
						"compiler=%s, " +
						"templateLoader=%s" +
				"}";
		// @formatter:on

		assertThat(mustacheCompiler).hasToString(String.format(
				expectedToString, hexIdentity(mustacheCompiler), compiler, templateLoader
		));
	}

	private static Compiler compiler() {
		return Mustache.compiler().zeroIsFalse(true).emptyStringIsFalse(true);
	}

	private static MustacheTemplateLoader mustacheTemplateLoader() {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		return new DefaultTemplateLoader(resourceLoader);
	}

	private static JMustacheCompiler jMustacheCompiler() {
		return jMustacheCompiler(mustacheTemplateLoader());
	}

	private static JMustacheCompiler jMustacheCompiler(MustacheTemplateLoader templateLoader) {
		return new JMustacheCompiler(compiler(), templateLoader);
	}

	private static JMustacheCompiler jMustacheCompiler(Compiler compiler, MustacheTemplateLoader templateLoader) {
		return new JMustacheCompiler(compiler, templateLoader);
	}

	private static Map<String, Object> model() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "foo");
		model.put("zero", 0);
		model.put("emptyString", "");
		return model;
	}
}
