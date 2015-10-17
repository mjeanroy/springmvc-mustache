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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.IOUtils.read;

/**
 * Template that will be evaluated using Nashorn engine.
 * This template will read once and then, evaluated with given data model.
 * Additional partials will be loaded during script evaluation.
 */
public class NashornTemplate implements MustacheTemplate {

	/**
	 * Nashorn engine.
	 * This engine must "know" a mustache implementation.
	 */
	private final ScriptEngine engine;

	/**
	 * Original template.
	 */
	private final String template;

	private final NashornPartialsObject partials;

	/**
	 * Create template.
	 *
	 * @param engine Script engine.
	 * @param reader Template.
	 */
	public NashornTemplate(ScriptEngine engine, Reader reader, NashornPartialsObject partials) {
		this.engine = engine;
		this.template = read(reader);
		this.partials = partials;
	}

	@Override
	public void execute(Map<String, Object> model, Writer writer) {
		try {
			String result = render(template, model);
			writer.write(result);
		}
		catch (IOException ex) {
			throw new MustacheIOException(ex);
		}
	}

	private String render(String template, Map<String, Object> model) {
		try {
			Invocable invocable = (Invocable) engine;
			return (String) invocable.invokeFunction("render", template, model, partials);
		}
		catch (ScriptException ex) {
			throw new NashornException(ex);
		}
		catch (NoSuchMethodException ex) {
			throw new NashornException(ex);
		}
	}
}
