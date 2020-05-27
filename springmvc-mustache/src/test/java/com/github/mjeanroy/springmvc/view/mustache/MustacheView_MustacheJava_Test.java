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

package com.github.mjeanroy.springmvc.view.mustache;

import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.MustacheJavaCompiler;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheFactory;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheResolver;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheResolver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static com.github.mjeanroy.springmvc.view.mustache.tests.StringTestUtils.joinLines;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MustacheView_MustacheJava_Test {

	private Map<String, Object> model;

	private MustacheTemplateLoader templateLoader;

	private MustacheView mustacheView;

	@Before
	public void setUp() {
		this.model = new HashMap<>();
		this.model.put("name", "foo");
		this.model.put("zero", 0);
		this.model.put("emptyString", "");

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		templateLoader = new DefaultTemplateLoader(resourceLoader);

		MustacheResolver mustacheResolver = new SpringMustacheResolver(templateLoader);
		MustacheFactory mustacheFactory = new SpringMustacheFactory(mustacheResolver, templateLoader);
		MustacheCompiler mustacheCompiler = new MustacheJavaCompiler(mustacheFactory, templateLoader);
		mustacheView = new MustacheView();
		mustacheView.setCompiler(mustacheCompiler);
	}

	@Test
	public void it_should_add_partial_aliases() {
		String k1 = "foo1";
		String v1 = "bar1";

		String k2 = "foo2";
		String v2 = "bar2";

		Map<String, String> aliases = new HashMap<>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		Map<String, String> partialsAliases = mustacheView.getAliases();
		assertThat(partialsAliases).isEmpty();

		mustacheView.addAliases(aliases);

		Map<String, String> newPartialsAliases = readField(mustacheView, "aliases");
		assertThat(newPartialsAliases).hasSize(aliases.size()).contains(
				entry(k1, v1),
				entry(k2, v2)
		);
	}

	@Test
	public void it_should_add_partial_alias() {
		String key = "foo";
		String value = "bar";

		Map<String, String> partialsAliases = mustacheView.getAliases();
		assertThat(partialsAliases).isNotNull().isEmpty();

		mustacheView.addAlias(key, value);

		Map<String, String> newPartialsAliases = readField(mustacheView, "aliases");
		assertThat(newPartialsAliases).isNotNull().hasSize(1).contains(
				entry(key, value)
		);
	}

	@Test
	public void it_should_render_template() throws Exception {
		Writer writer = givenWriter();
		HttpServletRequest request = givenHttpServletRequest();
		HttpServletResponse response = givenHttpServletResponse(writer);

		mustacheView.setUrl("/templates/foo.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = "<div>Hello foo</div>";
		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_zero_as_falsy() throws Exception {
		Writer writer = givenWriter();
		HttpServletRequest request = givenHttpServletRequest();
		HttpServletResponse response = givenHttpServletResponse(writer);

		mustacheView.setUrl("/templates/zero.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

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
	public void it_should_treat_empty_string_as_falsy() throws Exception {
		Writer writer = givenWriter();
		HttpServletRequest request = givenHttpServletRequest();
		HttpServletResponse response = givenHttpServletResponse(writer);

		mustacheView.setUrl("/templates/empty-string.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

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
	public void it_should_display_template_with_partial() throws Exception {
		Writer writer = givenWriter();
		HttpServletRequest request = givenHttpServletRequest();
		HttpServletResponse response = givenHttpServletResponse(writer);

		mustacheView.setUrl("/templates/composite.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix() throws Exception {
		Writer writer = givenWriter();
		HttpServletRequest request = givenHttpServletRequest();
		HttpServletResponse response = givenHttpServletResponse(writer);

		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");
		mustacheView.setUrl("/templates/composite-aliases.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix_event_with_full_name() throws Exception {
		Writer writer = givenWriter();
		HttpServletRequest request = givenHttpServletRequest();
		HttpServletResponse response = givenHttpServletResponse(writer);

		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");
		mustacheView.setUrl("/templates/composite.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_aliases() throws Exception {
		Writer writer = givenWriter();
		HttpServletRequest request = givenHttpServletRequest();
		HttpServletResponse response = givenHttpServletResponse(writer);

		mustacheView.addAlias("foo", "/templates/foo.template.html");
		mustacheView.setUrl("/templates/composite-aliases.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = joinLines(asList(
				"<div>",
				"	<div>Hello foo</div>",
				"</div>"
		));

		String result = writer.toString();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	private static Writer givenWriter() {
		return new StringWriter();
	}

	private static HttpServletRequest givenHttpServletRequest() {
		return mock(HttpServletRequest.class);
	}

	private static HttpServletResponse givenHttpServletResponse(Writer writer) {
		try {
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(response.getWriter()).thenReturn(new PrintWriter(writer));
			return response;
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}
}
