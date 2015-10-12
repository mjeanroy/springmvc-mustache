/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 <mickael.jeanroy@gmail.com>
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
import com.github.mjeanroy.springmvc.view.mustache.core.AbstractMustacheCompiler;

import javax.script.ScriptEngine;
import java.io.Reader;

import static com.github.mjeanroy.springmvc.view.mustache.commons.NashornUtils.getEngine;
import static java.util.Arrays.asList;

/**
 * Mustache compiler using nashorn engine to evaluation templates.
 * Internal implementation is original javascript mustache implementation.
 */
public class NashornCompiler extends AbstractMustacheCompiler implements MustacheCompiler {

	/**
	 * Internal Nashorn Engine.
	 */
	private final ScriptEngine engine;

	/**
	 * Create Nashorn Compiler.
	 *
	 * @param templateLoader Template Loader.
	 */
	public NashornCompiler(MustacheTemplateLoader templateLoader) {
		super(templateLoader);

		// Initialize nashorn engine
		this.engine = getEngine(asList(
				"/mustache/mustache.js",
				"/mustache/nashorn-bindings.js"
		));

		// Create loader and load it into global variable.
		NashornTemplateLoader nashornTemplateLoader = new NashornTemplateLoader(templateLoader);
		this.engine.put("$loader", nashornTemplateLoader);
	}

	@Override
	protected MustacheTemplate doCompile(String name) throws Exception {
		Reader reader = templateLoader.getTemplate(name);
		return new NashornTemplate(engine, reader);
	}
}
