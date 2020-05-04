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

package com.github.mjeanroy.springmvc.view.mustache.commons;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * Nashorn Utilities.
 */
public final class NashornUtils {

	private NashornUtils() {
	}

	/**
	 * Get new nashorn engine.
	 *
	 * @return Nashorn Engine.
	 */
	public static ScriptEngine getEngine() {
		return new ScriptEngineManager().getEngineByName("nashorn");
	}

	/**
	 * Get new nashorn engine.
	 *
	 * @param scripts List of scripts to evaluate.
	 * @return Nashorn Engine.
	 */
	public static ScriptEngine getEngine(Collection<InputStream> scripts) {
		ScriptEngine engine = getEngine();

		for (InputStream script : scripts) {
			evaluate(engine, script);
		}

		return engine;
	}

	private static void evaluate(ScriptEngine engine, InputStream script) {
		try {
			engine.eval(new InputStreamReader(script));
		}
		catch (ScriptException ex) {
			throw new NashornException(ex);
		}
	}
}
