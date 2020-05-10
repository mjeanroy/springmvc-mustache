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

package com.github.mjeanroy.springmvc.view.mustache.mustachejava;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import com.github.mjeanroy.springmvc.view.mustache.core.AbstractMustacheCompiler;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Mustache compiler.
 * This compiler use internally mustache.java as real implementation.
 */
public class MustacheJavaCompiler extends AbstractMustacheCompiler implements MustacheCompiler {

	/**
	 * Implementation of {@link MustacheFactory} used internally
	 * to compile mustache templates.
	 */
	private final MustacheFactory mustacheFactory;

	/**
	 * Build new compiler based on mustache.java.
	 * An instance of {@link SpringMustacheFactory} that used template loader is automatically created.
	 *
	 * @param templateLoader Template loader.
	 */
	public MustacheJavaCompiler(MustacheTemplateLoader templateLoader) {
		super(templateLoader);
		this.mustacheFactory = new SpringMustacheFactory(templateLoader);

		// Use a custom reflection object handler to "see" zero as a falsey value
		// See: https://github.com/spullara/mustache.java/pull/111
		SpringMustacheReflectionObjectHandler reflectionObjectHandler = new SpringMustacheReflectionObjectHandler();
		((SpringMustacheFactory) this.mustacheFactory).setObjectHandler(reflectionObjectHandler);
	}

	@Override
	protected MustacheTemplate doCompile(String name) {
		Mustache mustache = mustacheFactory.compile(name);
		return new MustacheJavaTemplate(mustache);
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("mustacheFactory", mustacheFactory)
				.append("templateLoader", templateLoader)
				.build();
	}
}
