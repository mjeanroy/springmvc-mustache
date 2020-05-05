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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStream;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.IOUtils.getFirstAvailableStream;
import static com.github.mjeanroy.springmvc.view.mustache.commons.IOUtils.getStream;
import static com.github.mjeanroy.springmvc.view.mustache.commons.NashornUtils.getEngine;
import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notNull;
import static java.util.Arrays.asList;

/**
 * Nashorn engine that can be used to render Mustache rendering.
 *
 * @deprecated Nashorn will be removed after jdk 11, so nashorn engine ill be removed in the next major version.
 */
@Deprecated
public class MustacheEngine {

	/**
	 * Get default mustache implementation.
	 *
	 * @return Stream associated to default mustache implementation.
	 */
	private static InputStream getDefault() {
		return getFirstAvailableStream(asList(
				// First, try with webjars
				"/META-INF/resources/webjars/mustache/**/mustache.js",
				"/META-INF/resources/npm/mustache/**/mustache.js",

				// Then, try inside a bower_components directory
				"/bower_components/mustache/**/mustache.js",

				// Then, try "generic" directory
				"/vendors/mustache/**/mustache.js",
				"/js/mustache/**/mustache.js"
		));
	}

	/**
	 * Internal Nashorn Engine.
	 */
	private final ScriptEngine engine;

	/**
	 * Partials object.
	 */
	private final NashornPartialsObject partials;

	/**
	 * Build nashorn engine with default mustache js implementation.
	 *
	 * Mustache file will be searched into classpath:
	 * - First, try to locate webjars.
	 * - Then, try to locate into bower_components directory.
	 * - Finally, try to locate into "generic" directory ("vendors" or "js" directories).
	 * If everything fail, an exception will be thrown.
	 *
	 * @param templateLoader Template loader, used to resolve partials.
	 * @throws com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException If everything fail.
	 */
	public MustacheEngine(MustacheTemplateLoader templateLoader) {
		this(templateLoader, getDefault());
	}

	/**
	 * Build nashorn engine with default mustache js implementation.
	 * Mustache file is given as a second parameter and is resolved against classpath.
	 *
	 * @param templateLoader Template loader, used to resolve partials.
	 * @param mustacheJs Mustache JavaScript file (as {@link InputStream} instance).
	 * @throws com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException If script does not exist.
	 */
	public MustacheEngine(MustacheTemplateLoader templateLoader, String mustacheJs) {
		this(templateLoader, getStream(mustacheJs));
	}

	/**
	 * Build nashorn engine with default mustache js implementation.
	 * Mustache file is given as a second parameter and is resolved against classpath.
	 *
	 * @param templateLoader Template loader, used to resolve partials.
	 * @param mustacheJs Mustache JavaScript file (as {@link InputStream} instance).
	 * @throws com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException If script does not exist.
	 */
	public MustacheEngine(MustacheTemplateLoader templateLoader, InputStream mustacheJs) {
		notNull(mustacheJs, "Mustache JS must not be null");
		notNull(templateLoader, "Template Loader must not be null");

		this.engine = getEngine(asList(mustacheJs, getStream("/mustache/nashorn-bindings.js")));
		this.partials = new NashornPartialsObject(templateLoader);
	}

	/**
	 * Render template with given model object.
	 *
	 * @param template Template.
	 * @param model Model object.
	 * @return Rendered template.
	 */
	public String render(String template, Map<String, Object> model) {
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
