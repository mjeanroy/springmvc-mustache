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
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.StringTestUtils.joinLines;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class MustacheJavaCompilerTest {

	@Test
	public void it_should_render_template() {
		String name = "/templates/foo.template.html";

		Writer writer = new StringWriter();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheJavaCompiler mustacheJavaCompiler = new MustacheJavaCompiler(templateLoader);

		MustacheTemplate template = mustacheJavaCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model(), writer);

		String expected = "<div>Hello foo</div>";
		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_zero_as_falsy() {
		String name = "/templates/zero.template.html";

		Writer writer = new StringWriter();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheJavaCompiler mustacheJavaCompiler = new MustacheJavaCompiler(templateLoader);

		MustacheTemplate template = mustacheJavaCompiler.compile(name);

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
		String name = "/templates/empty-string.template.html";

		Writer writer = new StringWriter();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheJavaCompiler mustacheJavaCompiler = new MustacheJavaCompiler(templateLoader);

		MustacheTemplate template = mustacheJavaCompiler.compile(name);

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
		String name = "/templates/composite.template.html";

		Writer writer = new StringWriter();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheJavaCompiler mustacheJavaCompiler = new MustacheJavaCompiler(templateLoader);

		MustacheTemplate template = mustacheJavaCompiler.compile(name);

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

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		MustacheJavaCompiler mustacheJavaCompiler = new MustacheJavaCompiler(templateLoader);

		String name = "/templates/composite-aliases.template.html";

		MustacheTemplate template = mustacheJavaCompiler.compile(name);

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

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		MustacheJavaCompiler mustacheJavaCompiler = new MustacheJavaCompiler(templateLoader);

		String name = "/templates/composite.template.html";

		MustacheTemplate template = mustacheJavaCompiler.compile(name);

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

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		MustacheJavaCompiler mustacheJavaCompiler = new MustacheJavaCompiler(templateLoader);

		String name = "/templates/composite-aliases.template.html";
		Map<String, String> aliases = singletonMap(
				"foo", "/templates/foo.template.html"
		);

		templateLoader.addTemporaryPartialAliases(aliases);

		MustacheTemplate template = mustacheJavaCompiler.compile(name);

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

	private static Map<String, Object> model() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "foo");
		model.put("zero", 0);
		model.put("emptyString", "");
		return model;
	}
}
