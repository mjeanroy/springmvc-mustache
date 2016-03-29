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

package com.github.mjeanroy.springmvc.view.mustache.core;

import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheCompilationException;

/**
 * Abstraction that define commons code to all mustache
 * compilers.
 */
public abstract class AbstractMustacheCompiler implements MustacheCompiler {

	private static final Logger log = LoggerFactory.getLogger(AbstractMustacheCompiler.class);

	/**
	 * Mustache template loader that will be used to load templates
	 * and partials.
	 */
	protected final MustacheTemplateLoader templateLoader;

	/**
	 * Build new compiler.
	 *
	 * @param templateLoader Template loader to use.
	 */
	protected AbstractMustacheCompiler(MustacheTemplateLoader templateLoader) {
		this.templateLoader = notNull(templateLoader, "Template loader must not be null");
	}

	@Override
	public MustacheTemplate compile(String name) {
		log.debug("Compile template: {}", name);
		notNull(name, "Template name must not be null");

		try {
			return doCompile(name);
		}
		catch (Exception ex) {
			throw new MustacheCompilationException(ex);
		}
	}

	/**
	 * Process template compilation.
	 * This methods should rethrows exception since it will be
	 * catches later (and a new {@link MustacheCompilationException} will
	 * be thrown).
	 *
	 * @param name Template name.
	 * @return Mustache template.
	 * @throws Exception If something bad happens (will be catched and rethrows).
	 */
	protected abstract MustacheTemplate doCompile(String name) throws Exception;

	@Override
	public void setPrefix(String prefix) {
		log.trace("Set compiler prefix: {}", prefix);
		templateLoader.setPrefix(notNull(prefix, "Prefix must not be null"));
	}

	@Override
	public void setSuffix(String suffix) {
		log.trace("Set compiler suffix: '{}'", suffix);
		templateLoader.setSuffix(notNull(suffix, "Suffix must not be null"));
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
	public void addTemporaryPartialAliases(Map<String, String> partialAliases) {
		notNull(partialAliases, "Partial aliases must not be null");

		log.debug("Add temporary partial aliases");
		if (log.isTraceEnabled()) {
			for (Map.Entry<String, String> entry : partialAliases.entrySet()) {
				log.trace("  => {} -> {}", entry.getKey(), entry.getValue());
			}
		}

		templateLoader.addTemporaryPartialAliases(partialAliases);
	}

	@Override
	public void removeTemporaryPartialAliases() {
		log.debug("Remove temporary partial aliases");
		templateLoader.removeTemporaryPartialAliases();
	}
}
