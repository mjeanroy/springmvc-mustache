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

package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import com.github.mjeanroy.springmvc.view.mustache.core.AbstractMustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.notNull;
import static com.samskivert.mustache.Mustache.Compiler;

/**
 * Mustache Compiler using JMustache as real implementation.
 */
public final class JMustacheCompiler extends AbstractMustacheCompiler {

	private static final Logger log = LoggerFactory.getLogger(JMustacheCompiler.class);

	/**
	 * Original JMustache compiler.
	 */
	private final Compiler compiler;

	/**
	 * Build new mustache compiler using JMustache API.
	 * This compiler need a {@link Compiler} to produce compiled template
	 * and a {@link MustacheTemplateLoader} to load partials defined in templates.
	 *
	 * @param compiler JMustache Compiler.
	 * @param templateLoader Template Loader.
	 */
	public JMustacheCompiler(Compiler compiler, MustacheTemplateLoader templateLoader) {
		super(templateLoader);
		this.compiler = notNull(compiler, "Compiler must not be null");
	}

	@Override
	protected MustacheTemplate doCompile(String name) {
		try(Reader template = templateLoader.getTemplate(name)) {
			final Template result = getTemplate(template, templateLoader);
			return new JMustacheTemplate(result);
		}
		catch (IOException ex) {
			log.error(ex.getMessage());
			throw new MustacheIOException(ex);
		}
	}

	protected Template getTemplate(Reader template, MustacheTemplateLoader templateLoader) {
		return compiler.withLoader(new JMustacheTemplateLoader(templateLoader)).compile(template);
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("compiler", compiler)
				.append("templateLoader", templateLoader)
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof JMustacheCompiler) {
			JMustacheCompiler c = (JMustacheCompiler) o;
			return Objects.equals(compiler, c.compiler) && Objects.equals(templateLoader, c.templateLoader);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(compiler, templateLoader);
	}
}
