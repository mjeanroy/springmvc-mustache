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

package com.github.mjeanroy.springmvc.view.mustache.nashorn;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.StringTestUtils.joinLines;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
public class NashornCompilerTest {

	@Test
	public void it_should_render_template() {
		NashornCompiler nashornCompiler = nashornCompiler();
		Writer writer = new StringWriter();
		String name = "/templates/foo.template.html";
		MustacheTemplate template = nashornCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isNotNull().isNotEmpty().isEqualTo("<div>Hello foo</div>");
	}

	@Test
	public void it_should_treat_zero_as_falsy() {
		NashornCompiler nashornCompiler = nashornCompiler();
		Writer writer = new StringWriter();
		String name = "/templates/zero.template.html";
		MustacheTemplate template = nashornCompiler.compile(name);

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
		NashornCompiler nashornCompiler = nashornCompiler();
		Writer writer = new StringWriter();
		String name = "/templates/empty-string.template.html";
		MustacheTemplate template = nashornCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"	",
				"	An empty string should be falsy.",
				"</div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial() {
		NashornCompiler nashornCompiler = nashornCompiler();
		Writer writer = new StringWriter();
		String name = "/templates/composite.template.html";
		MustacheTemplate template = nashornCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"<div>Hello foo</div></div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");

		NashornCompiler nashornCompiler = nashornCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";
		MustacheTemplate template = nashornCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"<div>Hello foo</div></div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix_event_with_full_name() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");

		NashornCompiler nashornCompiler = nashornCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite.template.html";
		MustacheTemplate template = nashornCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"<div>Hello foo</div></div>"
		)));
	}

	@Test
	public void it_should_display_template_with_partial_aliases() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		NashornCompiler nashornCompiler = nashornCompiler(templateLoader);

		Writer writer = new StringWriter();
		String name = "/templates/composite-aliases.template.html";
		Map<String, String> aliases = Collections.singletonMap(
				"foo", "/templates/foo.template.html"
		);

		templateLoader.addTemporaryPartialAliases(aliases);

		MustacheTemplate template = nashornCompiler.compile(name);

		template.execute(model(), writer);

		assertThat(writer.toString()).isEqualTo(joinLines(asList(
				"<div>",
				"<div>Hello foo</div></div>"
		)));
	}

	@Test
	public void it_should_implement_to_string() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		MustacheEngine mustacheEngine = mustacheEngine(templateLoader);
		NashornCompiler nashornCompiler = nashornCompiler(templateLoader, mustacheEngine);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.nashorn.NashornCompiler@%s{" +
						"engine=%s, " +
						"templateLoader=%s" +
				"}";
		// @formatter:on

		assertThat(nashornCompiler).hasToString(String.format(
				expectedToString, hexIdentity(nashornCompiler), mustacheEngine, templateLoader
		));
	}

	private static NashornCompiler nashornCompiler() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return nashornCompiler(resourceLoader);
	}

	private static NashornCompiler nashornCompiler(ResourceLoader resourceLoader) {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader(resourceLoader);
		MustacheEngine mustacheEngine = new MustacheEngine(templateLoader);
		return new NashornCompiler(templateLoader, mustacheEngine);
	}

	private static NashornCompiler nashornCompiler(MustacheTemplateLoader templateLoader) {
		MustacheEngine mustacheEngine = new MustacheEngine(templateLoader);
		return nashornCompiler(templateLoader, mustacheEngine);
	}

	private static NashornCompiler nashornCompiler(MustacheTemplateLoader templateLoader, MustacheEngine mustacheEngine) {
		return new NashornCompiler(templateLoader, mustacheEngine);
	}

	private static MustacheEngine mustacheEngine(MustacheTemplateLoader templateLoader) {
		return new MustacheEngine(templateLoader);
	}

	private static MustacheTemplateLoader mustacheTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return mustacheTemplateLoader(resourceLoader);
	}

	private static MustacheTemplateLoader mustacheTemplateLoader(ResourceLoader resourceLoader) {
		return new DefaultTemplateLoader(resourceLoader);
	}

	private static Map<String, Object> model() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "foo");
		model.put("zero", 0);
		model.put("emptyString", "");
		return model;
	}
}
