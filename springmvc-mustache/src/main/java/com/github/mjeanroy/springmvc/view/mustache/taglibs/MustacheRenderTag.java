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
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A JSP tag that can be used to render a mustache template.
 */
public class MustacheRenderTag extends TagSupport {

	/**
	 * Mustache compiler to use.
	 *
	 * If not specified, the compiler registered in current spring application context
	 * will be used.
	 */
	private MustacheCompiler compiler;

	/**
	 * Mustache template to render.
	 */
	private String template;

	/**
	 * Template parameters.
	 */
	private Map<String, Object> parameters;

	@Override
	public int doStartTag() {
		reset();
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		MustacheCompiler mustacheCompiler = lookupCompiler();

		// Fail fast
		if (template == null || template.isEmpty()) {
			throw new JspException(
					"The template parameter of mustache:render tag is mandatory and must not be empty"
			);
		}

		MustacheTemplate template = mustacheCompiler.compile(this.template);
		template.execute(parameters, pageContext.getOut());
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		reset();
	}

	/**
	 * Set {@link #compiler}
	 *
	 * @param compiler New {@link #compiler}
	 */
	public void setCompiler(MustacheCompiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * Set {@link #template}
	 *
	 * @param template New {@link #template}
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Add new mustache parameter.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 */
	void addParameter(String name, Object value) {
		parameters.put(name, value);
	}

	private MustacheCompiler lookupCompiler() throws JspException {
		if (compiler != null) {
			return compiler;
		}

		WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		MustacheCompiler mustacheCompiler = appContext.getBean(MustacheCompiler.class);

		// Fail fast
		if (mustacheCompiler == null) {
			throw new JspException(
					"Cannot locate mustache compiler, please specify it as a parameter or register an instance of MustacheCompiler to the spring context"
			);
		}

		return mustacheCompiler;
	}

	private void reset() {
		this.compiler = null;
		this.parameters = new LinkedHashMap<>();
	}
}
