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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.samskivert.mustache.Mustache;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.StringTestUtils.joinLines;
import static com.samskivert.mustache.Mustache.Compiler;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class JMustacheCompilerTest {

	private StringWriter writer;

	private Map<String, Object> model;

	private MustacheTemplateLoader templateLoader;

	private JMustacheCompiler mustacheCompiler;

	@Before
	public void setUp() {
		this.model = new HashMap<String, Object>();
		this.model.put("name", "foo");
		this.model.put("zero", 0);
		this.model.put("emptyString", "");

		Compiler compiler = Mustache.compiler().zeroIsFalse(true).emptyStringIsFalse(true);

		writer = new StringWriter();
		templateLoader = new DefaultTemplateLoader(new DefaultResourceLoader());
		mustacheCompiler = new JMustacheCompiler(compiler, templateLoader);
	}

	@Test
	public void it_should_render_template() {
		String name = "/templates/foo.template.html";

		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		String expected = "<div>Hello foo</div>";
		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_zero_as_falsy() {
		String name = "/templates/zero.template.html";

		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

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

		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

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

		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

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
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");
		String name = "/templates/composite-aliases.template.html";

		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

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
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");
		String name = "/templates/composite.template.html";

		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

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
		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put("foo", "/templates/foo.template.html");
		templateLoader.addTemporaryPartialAliases(aliases);

		String name = "/templates/composite-aliases.template.html";

		MustacheTemplate template = mustacheCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

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
		Mustache.Compiler compiler = Mustache.compiler();
		ResourceLoader resourceLoader = mock(ResourceLoader.class, "ResourceLoader");
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		JMustacheCompiler jMustacheCompiler = new JMustacheCompiler(compiler, templateLoader);

		String templateLoaderIdentity = hexIdentity(templateLoader);
		String jMustacheCompilerIdentity = hexIdentity(jMustacheCompiler);

		// @formatter:off
		assertThat(jMustacheCompiler).hasToString(
				"com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler@" + jMustacheCompilerIdentity + "{" +
						"compiler=" + compiler + ", " +
						"templateLoader=com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader@" + templateLoaderIdentity + "{" +
								"resourceLoader=ResourceLoader, " +
								"prefix=null, " +
								"suffix=null, " +
								"partialAliases={}, " +
								"temporaryPartialAliases={}" +
						"}" +
				"}"
		);
		// @formatter:on
	}
}
