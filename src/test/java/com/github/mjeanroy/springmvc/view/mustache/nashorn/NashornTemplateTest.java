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

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("deprecation")
public class NashornTemplateTest {

	@Rule
	public ExpectedException thrown = none();

	private String markup;

	private MustacheEngine scriptEngine;

	private NashornTemplate template;

	@Before
	public void setUp() throws Exception {
		markup = "<div>Hello {{name}}</div>";
		Reader reader = new StringReader("<div>Hello {{name}}</div>");

		scriptEngine = mock(MustacheEngine.class);
		template = new NashornTemplate(scriptEngine, reader);

		final ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
		nashorn.eval(new InputStreamReader(getClass().getResourceAsStream("/META-INF/resources/webjars/mustache/2.3.2/mustache.js")));
		nashorn.eval(new InputStreamReader(getClass().getResourceAsStream("/mustache/nashorn-bindings.js")));

		// Mock Nashorn Engine
		when(scriptEngine.render(anyString(), anyMapOf(String.class, Object.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				String tmpl = (String) invocationOnMock.getArguments()[0];

				@SuppressWarnings("unchecked")
				Map<String, Object> models = (Map<String, Object>) invocationOnMock.getArguments()[1];

				Invocable invocable = (Invocable) nashorn;
				return invocable.invokeFunction("render", tmpl, models);
			}
		});
	}

	@Test
	public void it_should_execute_template() throws Exception {
		Writer writer = new StringWriter();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "World");

		template.execute(map, writer);

		verify(scriptEngine).render(markup, map);
		assertThat(writer.toString())
				.isNotNull()
				.isNotEmpty()
				.isEqualTo("<div>Hello World</div>");
	}

	@Test
	public void it_should_catch_io_exception() throws Exception {
		thrown.expect(MustacheIOException.class);

		Writer writer = mock(Writer.class);
		doThrow(IOException.class).when(writer).write(anyString());

		template.execute(new HashMap<String, Object>(), writer);
	}
}
