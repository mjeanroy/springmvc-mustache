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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * A JSP tag that can be used to specify a mustache parameter.
 */
public class MustacheParamTag extends BodyTagSupport {

	/**
	 * Parameter name.
	 */
	private String name;

	/**
	 * Parameter value.
	 */
	private Object value;

	@Override
	public int doEndTag() throws JspException {
		Tag parentTag = getParent();

		// Looks like a bad usage.
		if (!(parentTag instanceof MustacheRenderTag)) {
			throw new JspException(
					"The mustache:param tag must be a descendant of mustache:render tag"
			);
		}

		// Name paramater is mandatory
		if (name == null || name.isEmpty()) {
			throw new JspException(
					"The name parameter of mustache:param tag is mandatory and must not be empty"
			);
		}

		MustacheRenderTag renderTag = (MustacheRenderTag) parentTag;
		renderTag.addParameter(name, value);

		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		this.name = null;
		this.value = null;
	}

	/**
	 * Set {@link #name}
	 *
	 * @param name New {@link #name}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set {@link #value}
	 *
	 * @param value New {@link #value}
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
