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
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("deprecation")
public class NashornTemplateTest {

	private NashornTemplate template;

	@Before
	public void setUp() throws Exception {
		Reader reader = new StringReader("<div>Hello {{name}}</div>");
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheEngine scriptEngine = new MustacheEngine(mustacheTemplateLoader);

		template = new NashornTemplate(scriptEngine, reader);

		ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
		nashorn.eval(new InputStreamReader(getClass().getResourceAsStream("/META-INF/resources/webjars/mustache/2.3.2/mustache.js")));
		nashorn.eval(new InputStreamReader(getClass().getResourceAsStream("/mustache/nashorn-bindings.js")));
	}

	@Test
	public void it_should_execute_template() {
		Reader reader = new StringReader("<div>Hello {{name}}</div>");
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheEngine scriptEngine = new MustacheEngine(mustacheTemplateLoader);
		NashornTemplate template = new NashornTemplate(scriptEngine, reader);

		Writer writer = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "World");

		template.execute(map, writer);

		assertThat(writer.toString()).isEqualTo("<div>Hello World</div>");
	}

	@Test
	public void it_should_implement_to_string() {
		Reader reader = new StringReader("<div>Hello {{name}}</div>");
		ResourceLoader resourceLoader = mock(ResourceLoader.class, "ResourceLoader");
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheEngine scriptEngine = new MustacheEngine(mustacheTemplateLoader);
		NashornTemplate template = new NashornTemplate(scriptEngine, reader);

		MustacheEngine mustacheEngine = readField(template, "engine");
		ScriptEngine engine = readField(mustacheEngine, "engine");

		// @formatter:off
		assertThat(template).hasToString(
				"com.github.mjeanroy.springmvc.view.mustache.nashorn.NashornTemplate{" +
						"template=\"<div>Hello {{name}}</div>\", " +
						"engine=com.github.mjeanroy.springmvc.view.mustache.nashorn.MustacheEngine{" +
								"engine=" + engine + ", " +
								"partials=com.github.mjeanroy.springmvc.view.mustache.nashorn.NashornPartialsObject{" +
										"templateLoader=com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader{" +
												"resourceLoader=ResourceLoader, " +
												"prefix=null, " +
												"suffix=null, " +
												"partialAliases={}, " +
												"temporaryPartialAliases={}" +
										"}" +
								"}" +
						"}" +
				"}"
		);
		// @formatter:on
	}
}
