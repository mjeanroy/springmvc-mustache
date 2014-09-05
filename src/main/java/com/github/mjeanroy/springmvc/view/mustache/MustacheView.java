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

import static org.springframework.util.Assert.notNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.view.AbstractTemplateView;

/**
 * Implementation of mustache view.
 * Use jmustache internally as template compiler.
 */
public class MustacheView extends AbstractTemplateView {

	/** Mustache compiler. */
	private MustacheCompiler compiler;

	/** List of aliases that map alias name to partial path. */
	private final Map<String, String> aliases;

	/**
	 * Build new view.
	 */
	public MustacheView() {
		setContentType("text/html; charset=utf-8");
		this.aliases = new HashMap<String, String>();
	}

	/**
	 * Set new mustache compiler that can be used to compile view.
	 *
	 * @param compiler Mustache compiler.
	 */
	public void setCompiler(MustacheCompiler compiler) {
		notNull(compiler);
		this.compiler = compiler;
	}

	/**
	 * Get compiler that will be used to compile view.
	 *
	 * @return Mustache compiler.
	 */
	public MustacheCompiler getCompiler() {
		return compiler;
	}

	/**
	 * Add partials mapping.
	 *
	 * @param aliases New aliases.
	 */
	public void addAliases(Map<String, String> aliases) {
		notNull(aliases);
		for (Map.Entry<String, String> entry : aliases.entrySet()) {
			addAlias(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Add alias mapping.
	 *
	 * @param key Partial key.
	 * @param value Partial name.
	 */
	public void addAlias(String key, String value) {
		notNull(key);
		notNull(value);
		this.aliases.put(key, value);
	}

	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		notNull(compiler);

		response.setContentType(getContentType());
		renderTemplate(model, response.getWriter());
	}

	private void renderTemplate(Map<String, Object> model, Writer writer) {
		MustacheTemplate template = compiler.compile(viewLayoutName(), aliases);
		template.execute(model, writer);
	}

	private String viewLayoutName() {
		return getUrl();
	}
}
