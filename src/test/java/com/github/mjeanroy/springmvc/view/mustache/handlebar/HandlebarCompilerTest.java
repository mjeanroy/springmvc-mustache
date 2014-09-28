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

package com.github.mjeanroy.springmvc.view.mustache.handlebar;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultMustacheTemplateLoader;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HandlebarCompilerTest {

	private static final String SEPARATOR = System.getProperty("line.separator");

	private MustacheTemplateLoader templateLoader;

	private HandlebarCompiler hbCompiler;

	private StringWriter writer;

	private Map<String, Object> model;

	@Before
	public void setUp() {
		this.model = new HashMap<String, Object>();
		this.model.put("name", "foo");
		this.model.put("zero", 0);
		this.model.put("emptyString", "");

		writer = new StringWriter();

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		templateLoader = new DefaultMustacheTemplateLoader(resourceLoader);
		Handlebars hb = new Handlebars();

		hbCompiler = new HandlebarCompiler(hb, templateLoader);
	}

	@Test
	public void it_should_build_a_compiler() throws Exception {
		MustacheTemplateLoader templateLoader = mock(MustacheTemplateLoader.class);

		Handlebars handlebars = mock(Handlebars.class);
		when(handlebars.with(any(TemplateLoader.class))).thenReturn(handlebars);

		HandlebarCompiler hbCompiler = new HandlebarCompiler(handlebars, templateLoader);

		Handlebars hb = (Handlebars) readField(hbCompiler, "handlebars", true);
		MustacheTemplateLoader tmplLoader = (MustacheTemplateLoader) readField(hbCompiler, "templateLoader", true);

		assertThat(hb).isNotNull().isSameAs(handlebars);
		assertThat(tmplLoader).isNotNull();

		ArgumentCaptor<HandlebarTemplateLoader> hbTemplateLoader = ArgumentCaptor.forClass(HandlebarTemplateLoader.class);
		verify(handlebars).with(hbTemplateLoader.capture());

		MustacheTemplateLoader hbInternalLoader = (MustacheTemplateLoader) readField(hbTemplateLoader.getValue(), "loader", true);
		assertThat(hbInternalLoader).isNotNull().isSameAs(tmplLoader);
	}

	@Test
	public void it_should_render_template() throws Exception {
		String name = "/templates/foo.template.html";

		MustacheTemplate template = hbCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		String expected = "" +
				"<div>Hello foo</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_zero_as_falsy() throws Exception {
		String name = "/templates/zero.template.html";

		MustacheTemplate template = hbCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		String expected = "" +
				"<div>" + SEPARATOR +
				"	" + SEPARATOR +
				"	Zero should be falsy." + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_empty_string_as_falsy() throws Exception {
		String name = "/templates/empty-string.template.html";

		MustacheTemplate template = hbCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		String expected = "" +
				"<div>" + SEPARATOR +
				"	" + SEPARATOR +
				"	An empty string should be falsy." + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	@Ignore("With Handlebars, partials must not start with '/'")
	public void it_should_display_template_with_partial() throws Exception {
		String name = "/templates/composite.template.html";

		MustacheTemplate template = hbCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		String expected = "" +
				"<div>" + SEPARATOR +
				"	<div>Hello foo</div>" + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix() throws Exception {
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");
		String name = "/templates/composite-aliases.template.html";

		MustacheTemplate template = hbCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		String expected = "" +
				"<div>" + SEPARATOR +
				"	<div>Hello foo</div>" + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	@Ignore("With Handlebars, partials must not start with '/'")
	public void it_should_display_template_with_partial_using_prefix_suffix_even_with_full_name() throws Exception {
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");
		String name = "/templates/composite.template.html";

		MustacheTemplate template = hbCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		String expected = "" +
				"<div>" + SEPARATOR +
				"	<div>Hello foo</div>" + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_aliases() throws Exception {
		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put("foo", "/templates/foo.template.html");
		hbCompiler.addTemporaryPartialAliases(aliases);

		String name = "/templates/composite-aliases.template.html";

		MustacheTemplate template = hbCompiler.compile(name);

		// Try to execute template to check real result
		template.execute(model, writer);

		hbCompiler.removeTemporaryPartialAliases();

		String expected = "" +
				"<div>" + SEPARATOR +
				"	<div>Hello foo</div>" + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}
}
