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

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheCompilationException;

import java.io.IOException;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notNull;

/**
 * Mustache compiler using Java Handlebar as real implementation.
 */
public class HandlebarCompiler implements MustacheCompiler {

	/**
	 * Handlebar compiler.
	 * This compiler will be used internally to compile template.
	 */
	private final Handlebars handlebars;

	/**
	 * Handlebar template loader implementation.
	 * This loader will be used internally to load templates by name.
	 */
	private final HandlebarTemplateLoader templateLoader;

	/**
	 * Build new mustache compiler using Handlebars API.
	 * This compiler need a {@link Handlebars} to produce compiled template
	 * and a {@link MustacheTemplateLoader} to load partials defined in templates.
	 *
	 * @param handlebars     Handlebars Compiler (must not be null).
	 * @param templateLoader Template Loader (must not be null).
	 */
	public HandlebarCompiler(Handlebars handlebars, MustacheTemplateLoader templateLoader) {
		notNull(templateLoader, "Template loader must not be null");
		notNull(handlebars, "Handlebars compiler must not be null");

		this.templateLoader = new HandlebarTemplateLoader(templateLoader);
		this.handlebars = handlebars.with(this.templateLoader);
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
			final Template template = handlebars.compile(name);
			return new HandlebarTemplate(template);
		}
		catch (IOException ex) {
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
}
