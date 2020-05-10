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

package com.github.mjeanroy.springmvc.view.mustache.nashorn;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import com.github.mjeanroy.springmvc.view.mustache.core.AbstractMustacheCompiler;

import java.io.Reader;

import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.notNull;

/**
 * Mustache compiler using nashorn engine to evaluation templates.
 * Internal implementation is original javascript mustache implementation.
 *
 * @deprecated Nashorn will be removed after jdk 11, so nashorn engine ill be removed in the next major version.
 */
@Deprecated
public class NashornCompiler extends AbstractMustacheCompiler implements MustacheCompiler {

	/**
	 * Internal Nashorn Engine.
	 */
	private final MustacheEngine engine;

	/**
	 * Create Nashorn Compiler.
	 *
	 * @param templateLoader Template Loader.
	 * @param engine Mustache JavaScript engine.
	 */
	public NashornCompiler(MustacheTemplateLoader templateLoader, MustacheEngine engine) {
		super(templateLoader);
		this.engine = notNull(engine, "Mustache Engine must not be null");
	}

	@Override
	protected MustacheTemplate doCompile(String name) {
		Reader reader = templateLoader.getTemplate(name);
		return new NashornTemplate(engine, reader);
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("engine", engine)
				.append("templateLoader", templateLoader)
				.build();
	}
}
