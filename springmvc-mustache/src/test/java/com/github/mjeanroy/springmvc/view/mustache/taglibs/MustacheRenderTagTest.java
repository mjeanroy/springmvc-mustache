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

package com.github.mjeanroy.springmvc.view.mustache.taglibs;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler;
import com.samskivert.mustache.Mustache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.jsp.JspException;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MustacheRenderTagTest {

	private WebApplicationContext applicationContext;
	private MockHttpServletResponse response;
	private MustacheRenderTag tag;

	@BeforeEach
	void setUp() {
		applicationContext = mock(WebApplicationContext.class);

		MockServletContext servletContext = new MockServletContext();
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
		MockPageContext pageContext = new MockPageContext(servletContext);

		response = (MockHttpServletResponse) pageContext.getResponse();
		tag = new MustacheRenderTag();
		tag.setPageContext(pageContext);
		tag.doStartTag();
	}

	@Test
	void it_should_render_tag() throws Exception {
		Mustache.Compiler compiler = Mustache.compiler();
		ResourceLoader resourceLoader = new DefaultResourceLoader();

		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		MustacheCompiler mustacheCompiler = new JMustacheCompiler(compiler, templateLoader);

		when(applicationContext.getBean(MustacheCompiler.class)).thenReturn(mustacheCompiler);

		String parameterName = "name";
		String parameterValue = "John Doe";

		int r1 = tag.doStartTag();

		tag.setTemplate("foo");
		tag.addParameter(parameterName, parameterValue);

		int r2 = tag.doEndTag();

		assertThat(r1).isEqualTo(1);
		assertThat(r2).isEqualTo(6);
		assertThat(response.getContentAsString()).isEqualTo("<div>Hello John Doe</div>");
	}

	@Test
	void it_should_render_tag_using_specific_compiler() throws Exception {
		Mustache.Compiler compiler = Mustache.compiler();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader customTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheCompiler customMustacheCompiler = new JMustacheCompiler(compiler, customTemplateLoader);

		String parameterName = "name";
		String parameterValue = "John Doe";

		int r1 = tag.doStartTag();

		tag.setTemplate("/templates/foo.template.html");
		tag.setCompiler(customMustacheCompiler);
		tag.addParameter(parameterName, parameterValue);

		int r2 = tag.doEndTag();

		assertThat(r1).isEqualTo(1);
		assertThat(r2).isEqualTo(6);
		assertThat(response.getContentAsString()).isEqualTo("<div>Hello John Doe</div>");
	}

	@Test
	void it_should_render_tag_and_release() throws Exception {
		Mustache.Compiler compiler = Mustache.compiler();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader customTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheCompiler customMustacheCompiler = new JMustacheCompiler(compiler, customTemplateLoader);

		String parameterName = "name";
		String parameterValue = "John Doe";

		tag.doStartTag();
		tag.setTemplate("/templates/foo.template.html");
		tag.setCompiler(customMustacheCompiler);
		tag.addParameter(parameterName, parameterValue);
		tag.doEndTag();
		tag.release();

		Map<String, Object> parameters = readField(tag, "parameters");

		assertThat(response.getContentAsString()).isEqualTo("<div>Hello John Doe</div>");
		assertThat(parameters).isEmpty();
	}

	@Test
	void it_should_fail_to_render_tag_without_template() {
		Mustache.Compiler compiler = Mustache.compiler();
		ResourceLoader resourceLoader = new DefaultResourceLoader();

		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		MustacheCompiler globalMustacheCompiler = new JMustacheCompiler(compiler, templateLoader);
		when(applicationContext.getBean(MustacheCompiler.class)).thenReturn(globalMustacheCompiler);

		tag.doStartTag();

		assertThatThrownBy(tag::doEndTag)
				.isInstanceOf(JspException.class)
				.hasMessage("The template parameter of mustache:render tag is mandatory and must not be empty");
	}

	@Test
	void it_should_fail_to_render_tag_without_any_compiler() {
		tag.doStartTag();

		assertThatThrownBy(tag::doEndTag)
				.isInstanceOf(JspException.class)
				.hasMessage("Cannot locate mustache compiler, please specify it as a parameter or register an instance of MustacheCompiler to the spring context");
	}
}
