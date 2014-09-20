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

import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.github.jknack.handlebars.Template;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheCompilationException;

/**
 * Implementation of {@link MustacheTemplate} using Java Handlebar
 * as real template implementation.
 */
public class HandlebarTemplate implements MustacheTemplate {

	/**
	 * Original handlebar template that will be used to render
	 * mustache template.
	 */
	private final Template template;

	/**
	 * Build new handlebar template.
	 *
	 * @param template Original handlebar template.
	 */
	public HandlebarTemplate(Template template) {
		notNull(template, "Template must not be null");
		this.template = template;
	}

	@Override
	public void execute(Map<String, Object> model, Writer writer) {
		try {
			template.apply(model, writer);
		}
		catch (IOException ex) {
			throw new MustacheCompilationException(ex);
		}
	}
}
