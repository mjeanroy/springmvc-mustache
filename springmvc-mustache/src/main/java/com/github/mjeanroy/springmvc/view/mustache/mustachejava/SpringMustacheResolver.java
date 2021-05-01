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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheTemplateNotFoundException;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.github.mustachejava.resolver.DefaultResolver;

import java.io.Reader;

public final class SpringMustacheResolver extends DefaultResolver {

	private static final Logger log = LoggerFactory.getLogger(SpringMustacheResolver.class);

	/**
	 * Mustache template loader that will load
	 * templates and partials.
	 * This template loader will also be used to resolve template
	 * location from name.
	 */
	private final MustacheTemplateLoader templateLoader;

	/**
	 * Build new mustache resolver.
	 *
	 * @param templateLoader Template loader to use.
	 */
	public SpringMustacheResolver(MustacheTemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	@Override
	public Reader getReader(String resourceName) {
		log.debug("Load template associated to resource: {}", resourceName);

		try {
			return templateLoader.getTemplate(resourceName);
		}
		catch (MustacheTemplateNotFoundException ex) {
			return super.getReader(resourceName);
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("templateLoader", templateLoader)
				.build();
	}
}
