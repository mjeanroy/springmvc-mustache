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

import static com.samskivert.mustache.Mustache.Compiler;
import static java.util.Collections.emptyMap;

import java.io.Reader;
import java.util.Map;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheCompilationException;
import com.samskivert.mustache.Template;

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
	 * and a {@link JMustacheTemplateLoader} to load partials defined in templates.
	 *
	 * @param compiler JMustache Compiler.
	 * @param templateLoader Template Loader.
	 */
	public JMustacheCompiler(Compiler compiler, MustacheTemplateLoader templateLoader) {
		this.compiler = compiler;
		this.templateLoader = templateLoader;
	}

	@Override
	public void setPrefix(String prefix) {
		templateLoader.setPrefix(prefix);
	}

	@Override
	public void setSuffix(String suffix) {
		templateLoader.setSuffix(suffix);
	}

	@Override
	public MustacheTemplate compile(String name) {
		Map<String, String> aliases = emptyMap();
		return compile(name, aliases);
	}

	@Override
	public MustacheTemplate compile(String name, Map<String, String> aliases) {
		final MustacheTemplateLoader templateLoader;

		// If aliases is not empty, we should use a template loader that define
		// these aliases only for this compilation.
		if (!aliases.isEmpty()) {
			templateLoader = this.templateLoader.clone();
			templateLoader.addPartialAliases(aliases);
		} else {
			templateLoader = this.templateLoader;
		}

		try {
			final Reader template = templateLoader.getTemplate(name);
			final Template result = getTemplate(template, templateLoader);
			return new JMustacheTemplate(result);
		}
		catch (Exception ex) {
			throw new MustacheCompilationException(ex);
		}
	}

	private Template getTemplate(Reader template, MustacheTemplateLoader templateLoader) {
		return compiler
				.withLoader(new JMustacheTemplateLoader(templateLoader))
				.compile(template);
	}
}
