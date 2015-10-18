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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.script.ScriptEngine;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;

public class NashornEngineTest {

	@Rule
	public ExpectedException thrown = none();

	private MustacheTemplateLoader templateLoader;

	@Before
	public void setUp() {
		templateLoader = mock(MustacheTemplateLoader.class);
	}

	@Test
	public void it_should_create_default_engine() throws Exception {
		MustacheEngine engine = new MustacheEngine(templateLoader);

		NashornPartialsObject partials = (NashornPartialsObject) readField(engine, "partials", true);
		assertThat(partials).isNotNull();
		assertThat((MustacheTemplateLoader) readField(partials, "templateLoader", true))
				.isNotNull()
				.isSameAs(templateLoader);

		assertThat((ScriptEngine) readField(engine, "engine", true))
				.isNotNull();
	}

	@Test
	public void it_should_create_with_given_scripts() throws Exception {
		String script = "/META-INF/resources/webjars/mustache/**/mustache.js";
		MustacheEngine engine = new MustacheEngine(templateLoader, script);

		NashornPartialsObject partials = (NashornPartialsObject) readField(engine, "partials", true);
		assertThat(partials).isNotNull();
		assertThat((MustacheTemplateLoader) readField(partials, "templateLoader", true))
				.isNotNull()
				.isSameAs(templateLoader);

		assertThat((ScriptEngine) readField(engine, "engine", true))
				.isNotNull();
	}

	@Test
	public void it_should_create_with_given_stream() throws Exception {
		InputStream stream = getClass().getResourceAsStream("/META-INF/resources/webjars/mustache/2.2.0/mustache.js");
		MustacheEngine engine = new MustacheEngine(templateLoader, stream);

		NashornPartialsObject partials = (NashornPartialsObject) readField(engine, "partials", true);
		assertThat(partials).isNotNull();
		assertThat((MustacheTemplateLoader) readField(partials, "templateLoader", true))
				.isNotNull()
				.isSameAs(templateLoader);

		assertThat((ScriptEngine) readField(engine, "engine", true))
				.isNotNull();
	}

	@Test
	public void it_should_execute_mustache() throws Exception {
		InputStream stream = getClass().getResourceAsStream("/META-INF/resources/webjars/mustache/2.2.0/mustache.js");
		MustacheEngine engine = new MustacheEngine(templateLoader, stream);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "foo");

		String result = engine.render("<div>Hello {{name}}</div>", map);

		assertThat(result)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo("<div>Hello foo</div>");
	}

	@Test
	public void it_should_execute_mustache_and_catch_script_error() throws Exception {
		thrown.expect(NashornException.class);

		InputStream stream = getClass().getResourceAsStream("/scripts/render-with-error.js");
		MustacheEngine engine = new MustacheEngine(templateLoader, stream);
		engine.render("<div>Hello {{name}}</div>", new HashMap<String, Object>());
	}

	@Test
	public void it_should_execute_mustache_and_catch_no_method_error() throws Exception {
		thrown.expect(NashornException.class);

		InputStream stream = getClass().getResourceAsStream("/scripts/test-constant.js");
		MustacheEngine engine = new MustacheEngine(templateLoader, stream);
		engine.render("<div>Hello {{name}}</div>", new HashMap<String, Object>());
	}
}
