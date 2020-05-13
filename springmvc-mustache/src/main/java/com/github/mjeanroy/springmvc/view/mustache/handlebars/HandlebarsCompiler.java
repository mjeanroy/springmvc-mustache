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

package com.github.mjeanroy.springmvc.view.mustache.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.Objects;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import com.github.mjeanroy.springmvc.view.mustache.core.AbstractMustacheCompiler;

import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.notNull;

/**
 * Mustache compiler using Java Handlebar as real implementation.
 */
public final class HandlebarsCompiler extends AbstractMustacheCompiler implements MustacheCompiler {

	/**
	 * Handlebar compiler.
	 * This compiler will be used internally to compile template.
	 */
	private final Handlebars handlebars;

	/**
	 * Build new mustache compiler using Handlebars API.
	 * This compiler need a {@link Handlebars} to produce compiled template
	 * and a {@link MustacheTemplateLoader} to load partials defined in templates.
	 *
	 * @param handlebars     Handlebars Compiler (must not be null).
	 * @param templateLoader Template Loader (must not be null).
	 */
	public HandlebarsCompiler(Handlebars handlebars, MustacheTemplateLoader templateLoader) {
		super(templateLoader);

		this.handlebars = notNull(handlebars, "Handlebars compiler must not be null").with(
				new HandlebarsTemplateLoader(templateLoader)
		);
	}

	@Override
	protected MustacheTemplate doCompile(String name) throws Exception {
		final Template template = handlebars.compile(name);
		return new HandlebarsTemplate(template);
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("handlebars", handlebars)
				.append("templateLoader", templateLoader)
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HandlebarsCompiler) {
			HandlebarsCompiler c = (HandlebarsCompiler) o;
			return Objects.equals(handlebars, c.handlebars) && Objects.equals(templateLoader, c.templateLoader);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(handlebars, templateLoader);
	}
}
