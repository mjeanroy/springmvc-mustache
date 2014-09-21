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

package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheCompilationException;
import com.samskivert.mustache.Template;

import java.io.Reader;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notNull;
import static com.samskivert.mustache.Mustache.Compiler;

/**
 * Mustache Compiler using JMustache as real implementation.
 */
public class JMustacheCompiler implements MustacheCompiler {

	/**
	 * Original JMustache compiler.
	 */
	private final Compiler compiler;

	/**
	 * Original JMustache template loader.
	 */
	private final MustacheTemplateLoader templateLoader;

	/**
	 * Build new mustache compiler using JMustache API.
	 * This compiler need a {@link Compiler} to produce compiled template
	 * and a {@link MustacheTemplateLoader} to load partials defined in templates.
	 *
	 * @param compiler JMustache Compiler.
	 * @param templateLoader Template Loader.
	 */
	public JMustacheCompiler(Compiler compiler, MustacheTemplateLoader templateLoader) {
		this.compiler = notNull(compiler, "Compiler must not be null");
		this.templateLoader = notNull(templateLoader, "Template loader must not be null");
	}

	@Override
	public void setPrefix(String prefix) {
		notNull(prefix, "Prefix must not be null");
		templateLoader.setPrefix(prefix);
	}

	@Override
	public void setSuffix(String suffix) {
		notNull(suffix, "Suffix must not be null");
		templateLoader.setSuffix(suffix);
	}

	@Override
	public String getPrefix() {
		return templateLoader.getPrefix();
	}

	@Override
	public String getSuffix() {
		return templateLoader.getSuffix();
	}

	@Override
	public MustacheTemplate compile(String name) {
		notNull(name, "Template name must not be null");

		try {
			final Reader template = templateLoader.getTemplate(name);
			final Template result = getTemplate(template, templateLoader);
			return new JMustacheTemplate(result);
		}
		catch (Exception ex) {
			throw new MustacheCompilationException(ex);
		}
	}

	@Override
	public void addTemporaryPartialAliases(Map<String, String> partialAliases) {
		notNull(partialAliases, "Partial aliases must not be null");
		templateLoader.addTemporaryPartialAliases(partialAliases);
	}

	@Override
	public void removeTemporaryPartialAliases() {
		templateLoader.removeTemporaryPartialAliases();
	}

	private Template getTemplate(Reader template, MustacheTemplateLoader templateLoader) {
		return compiler
				.withLoader(new JMustacheTemplateLoader(templateLoader))
				.compile(template);
	}
}
