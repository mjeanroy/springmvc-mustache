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
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.github.mustachejava.DefaultMustacheFactory;

import java.io.Reader;

/**
 * Implementation of mustache factory that use a template loader
 * internally.
 *
 * Future release of mustache.java will support custom template resolver, this
 * implementation will be useless.
 * See: https://github.com/spullara/mustache.java/pull/105
 */
class SpringMustacheFactory extends DefaultMustacheFactory {

	private static final Logger log = LoggerFactory.getLogger(SpringMustacheFactory.class);

	/**
	 * Mustache template loader that will load
	 * templates and partials.
	 * This template loader will also be used to resolve template
	 * location from name.
	 */
	private final MustacheTemplateLoader templateLoader;

	/**
	 * Build new mustache factory.
	 *
	 * @param templateLoader Template loader to use.
	 */
	public SpringMustacheFactory(MustacheTemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	@Override
	public Reader getReader(String resourceName) {
		log.debug("Load template associated to resource: {}", resourceName);
		return templateLoader.getTemplate(resourceName);
	}

	@Override
	public String resolvePartialPath(String dir, String name, String extension) {
		// Future release of mustache.java will be prevent dir and extension being
		// always added to template name
		// See: https://github.com/spullara/mustache.java/pull/110
		// For now, this method has to be overridden
		log.debug("Resolve partial path for name: {}", name);
		return templateLoader.resolve(name);
	}
}
