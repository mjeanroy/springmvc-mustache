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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockPageContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

class MustacheParameterTagTest {

	private MustacheRenderTag parentTag;
	private MustacheParamTag tag;

	@BeforeEach
	void setUp() {
		PageContext pageContext = new MockPageContext();

		parentTag = new MustacheRenderTag();
		parentTag.setPageContext(pageContext);
		parentTag.doStartTag();

		tag = new MustacheParamTag();
		tag.setPageContext(pageContext);
		tag.setParent(parentTag);
	}

	@Test
	void it_should_render_tag() throws Exception {
		String parameterName = "fullName";
		String parameterValue = "John Doe";

		tag.setName(parameterName);
		tag.setValue(parameterValue);

		int result = tag.doEndTag();

		assertThat(result).isEqualTo(6);

		Map<String, Object> parameters = readField(parentTag, "parameters");
		assertThat(parameters).hasSize(1).contains(
				entry(parameterName, parameterValue)
		);
	}

	@Test
	void it_should_render_tag_and_release_it() throws Exception {
		String parameterName = "fullName";
		String parameterValue = "John Doe";

		tag.setName(parameterName);
		tag.setValue(parameterValue);

		int result = tag.doEndTag();

		assertThat(result).isEqualTo(6);
		assertThat(readField(tag, "name", String.class)).isEqualTo(parameterName);
		assertThat(readField(tag, "value", Object.class)).isEqualTo(parameterValue);

		tag.release();
		assertThat(readField(tag, "name", String.class)).isNull();
		assertThat(readField(tag, "value", Object.class)).isNull();
	}

	@Test
	void it_should_fail_to_render_tag_if_parent_tag_is_null() {
		tag.setParent(null);
		tag.setName("fullName");
		tag.setValue("John Doe");

		assertThatThrownBy(tag::doEndTag)
				.isInstanceOf(JspException.class)
				.hasMessage("The mustache:param tag must be a descendant of mustache:render tag");
	}

	@Test
	void it_should_fail_to_render_tag_if_parameter_name_is_null() {
		tag.setName(null);
		tag.setValue("John Doe");

		assertThatThrownBy(tag::doEndTag)
				.isInstanceOf(JspException.class)
				.hasMessage("The name parameter of mustache:param tag is mandatory and must not be empty");
	}

	@Test
	void it_should_fail_to_render_tag_if_parameter_name_is_empty() {
		tag.setName("");
		tag.setValue("John Doe");

		assertThatThrownBy(tag::doEndTag)
				.isInstanceOf(JspException.class)
				.hasMessage("The name parameter of mustache:param tag is mandatory and must not be empty");
	}
}
