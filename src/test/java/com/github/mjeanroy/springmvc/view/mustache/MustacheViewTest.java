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

package com.github.mjeanroy.springmvc.view.mustache;

import com.github.mjeanroy.springmvc.view.mustache.core.DefaultMustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler;
import com.samskivert.mustache.Mustache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.samskivert.mustache.Mustache.Compiler;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class MustacheViewTest {

	private static final String SEPARATOR = System.getProperty("line.separator");

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	private StringWriter writer;

	private Map<String, Object> model;

	private MustacheTemplateLoader templateLoader;

	private MustacheView mustacheView;

	@Before
	public void setUp() throws Exception {
		this.model = new HashMap<String, Object>();
		this.model.put("name", "foo");
		this.model.put("zero", 0);
		this.model.put("emptyString", "");

		writer = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(writer));

		templateLoader = new DefaultMustacheTemplateLoader();
		Compiler compiler = Mustache.compiler()
				.zeroIsFalse(true)
				.emptyStringIsFalse(true);

		MustacheCompiler mustacheCompiler = new JMustacheCompiler(compiler, templateLoader);
		mustacheView = new MustacheView();
		mustacheView.setCompiler(mustacheCompiler);
	}

	@Test
	public void it_should_add_partial_aliases() throws Exception {
		String k1 = "foo1";
		String v1 = "bar1";

		String k2 = "foo2";
		String v2 = "bar2";

		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put(k1, v1);
		aliases.put(k2, v2);

		Map<String, String> partialsAliases = mustacheView.getAliases();
		assertThat(partialsAliases)
				.isNotNull()
				.isEmpty();

		mustacheView.addAliases(aliases);

		partialsAliases = (Map<String, String>) readField(mustacheView, "aliases", true);
		assertThat(partialsAliases)
				.isNotNull()
				.hasSize(aliases.size())
				.contains(
						entry(k1, v1),
						entry(k2, v2)
				);
	}

	@Test
	public void it_should_add_partial_alias() throws Exception {
		String key = "foo";
		String value = "bar";

		Map<String, String> partialsAliases = mustacheView.getAliases();
		assertThat(partialsAliases).isNotNull().isEmpty();

		mustacheView.addAlias(key, value);

		partialsAliases = (Map<String, String>) readField(mustacheView, "aliases", true);
		assertThat(partialsAliases).isNotNull().hasSize(1).contains(
				entry(key, value)
		);
	}

	@Test
	public void it_should_render_template() throws Exception {
		mustacheView.setUrl("/templates/foo.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = "" +
				"<div>Hello foo</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_treat_zero_as_falsy() throws Exception {
		mustacheView.setUrl("/templates/zero.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

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
		mustacheView.setUrl("/templates/empty-string.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = "" +
				"<div>" + SEPARATOR +
				"	" + SEPARATOR +
				"	An empty string should be falsy." + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial() throws Exception {
		mustacheView.setUrl("/templates/composite.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

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
		mustacheView.setUrl("/templates/composite-aliases.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = "" +
				"<div>" + SEPARATOR +
				"	<div>Hello foo</div>" + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_using_prefix_suffix_event_with_full_name() throws Exception {
		templateLoader.setPrefix("/templates/");
		templateLoader.setSuffix(".template.html");
		mustacheView.setUrl("/templates/composite.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = "" +
				"<div>" + SEPARATOR +
				"	<div>Hello foo</div>" + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void it_should_display_template_with_partial_aliases() throws Exception {
		mustacheView.addAlias("foo", "/templates/foo.template.html");
		mustacheView.setUrl("/templates/composite-aliases.template.html");
		mustacheView.renderMergedTemplateModel(model, request, response);

		verify(response).getWriter();

		String expected = "" +
				"<div>" + SEPARATOR +
				"	<div>Hello foo</div>" + SEPARATOR +
				"</div>";

		String result = writer.toString();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(expected);
	}
}
