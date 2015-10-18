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
import com.github.mjeanroy.springmvc.view.mustache.commons.IOUtils;
import com.github.mjeanroy.springmvc.view.mustache.core.AbstractMustacheCompiler;

import javax.script.ScriptEngine;
import java.io.InputStream;
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
	 * Partials object.
	 */
	private final NashornPartialsObject partials;

	/**
	 * Create Nashorn Compiler.
	 *
	 * @param templateLoader Template Loader.
	 */
	public NashornCompiler(MustacheTemplateLoader templateLoader) {
		super(templateLoader);

		InputStream bindings = IOUtils.getStream("/mustache/nashorn-bindings.js");
		InputStream mustacheJs = IOUtils.getFirstAvailableStream(asList(
				// First, try with webjars
				"/META-INF/resources/webjars/mustache/**/mustache.js",
				"/META-INF/resources/npm/mustache/**/mustache.js",

				// Then, try inside a bower_components directory
				"/bower_components/mustache/**/mustache.js",

				// Then, try "generic" directory
				"/vendors/mustache/**/mustache.js",
				"/js/mustache/**/mustache.js"
		));

		// Initialize nashorn engine
		this.engine = getEngine(asList(mustacheJs, bindings));

		// Initialize partials object
		this.partials = new NashornPartialsObject(templateLoader);
	}

	@Override
	protected MustacheTemplate doCompile(String name) throws Exception {
		Reader reader = templateLoader.getTemplate(name);
		return new NashornTemplate(engine, reader, partials);
	}

}
