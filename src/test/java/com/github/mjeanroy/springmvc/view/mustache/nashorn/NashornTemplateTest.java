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

import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

public class NashornTemplateTest {

	private ScriptEngine scriptEngine;

	private Reader reader;

	private NashornTemplate template;

	@Before
	public void setUp() throws Exception {
		NashornPartialsObject partials = mock(NashornPartialsObject.class);

		scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
		reader = new StringReader("<div>Hello {{name}}</div>");
		template = new NashornTemplate(scriptEngine, reader, partials);
	}

	@Test
	public void it_should_execute_template() throws Exception {
		URL urlMustache = getClass().getResource("/mustache/mustache.js");
		scriptEngine.eval(new FileReader(new File(urlMustache.toURI())));

		URL urlBinding = getClass().getResource("/mustache/nashorn-bindings.js");
		scriptEngine.eval(new FileReader(new File(urlBinding.toURI())));

		Writer writer = new StringWriter();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "World");

		template.execute(map, writer);

		assertThat(writer.toString())
				.isNotNull()
				.isNotEmpty()
				.isEqualTo("<div>Hello World</div>");
	}

	@Test
	public void it_should_fail_if_method_does_not_exist() {
		Writer writer = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "World");

		try {
			template.execute(map, writer);
			fail("Nashorn Exception should be thrown");
		}
		catch (Exception ex) {
			assertThat(ex)
					.isExactlyInstanceOf(NashornException.class)
					.hasCauseExactlyInstanceOf(NoSuchMethodException.class);
		}
	}

	@Test
	public void it_should_fail_if_script_fail() throws Exception {
		URL urlBinding = getClass().getResource("/mustache/nashorn-bindings.js");
		scriptEngine.eval(new FileReader(new File(urlBinding.toURI())));

		Writer writer = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "World");

		try {
			template.execute(map, writer);
			fail("Nashorn Exception should be thrown");
		}
		catch (Exception ex) {
			assertThat(ex)
					.isExactlyInstanceOf(NashornException.class)
					.hasCauseExactlyInstanceOf(ScriptException.class);
		}
	}
}
