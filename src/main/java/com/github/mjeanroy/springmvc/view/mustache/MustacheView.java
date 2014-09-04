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

import static com.samskivert.mustache.Mustache.Compiler;
import static com.samskivert.mustache.Mustache.TemplateLoader;
import static org.springframework.util.Assert.notNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.view.AbstractTemplateView;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheTemplateNotFoundException;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheTemplateLoader;
import com.samskivert.mustache.Template;

/**
 * Implementation of mustache view.
 * Use jmustache internally as template compiler.
 */
public class MustacheView extends AbstractTemplateView {

	/** Mustache compiler. */
	private Compiler compiler;

	/** Mustache template loader. */
	private JMustacheTemplateLoader templateLoader;

	/** List of aliases that map alias name to partial path. */
	private final Map<String, String> aliases;

	/**
	 * Build new view.
	 */
	public MustacheView() {
		setContentType("text/html; charset=utf-8");
		this.aliases = new HashMap<String, String>();
	}

	public Compiler getCompiler() {
		return compiler;
	}

	public JMustacheTemplateLoader getTemplateLoader() {
		return templateLoader;
	}

	public void setCompiler(Compiler compiler) {
		notNull(compiler);
		this.compiler = compiler;
	}

	public void setTemplateLoader(JMustacheTemplateLoader templateLoader) {
		notNull(templateLoader);
		this.templateLoader = templateLoader;
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
		notNull(templateLoader);

		response.setContentType(getContentType());
		renderTemplate(model, response.getWriter());
	}

	private void renderTemplate(Map<String, Object> model, Writer writer) {
		JMustacheTemplateLoader templateLoader = this.templateLoader.clone();
		templateLoader.addPartialAliases(aliases);

		Template template = getTemplate(templateLoader);
		template.execute(model, writer);
	}

	private Template getTemplate(TemplateLoader templateLoader) {
		try {
			String name = viewLayoutName();
			Reader template = templateLoader.getTemplate(name);
			return compiler
					.withLoader(templateLoader)
					.compile(template);
		}
		catch (Exception ex) {
			logger.trace(ex.getMessage());
			throw new MustacheTemplateNotFoundException(ex);
		}
	}

	private String viewLayoutName() {
		return getUrl();
	}
}
