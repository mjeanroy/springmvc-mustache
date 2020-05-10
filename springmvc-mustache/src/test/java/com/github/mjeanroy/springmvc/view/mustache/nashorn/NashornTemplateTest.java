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
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
public class NashornTemplateTest {

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
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheEngine mustacheEngine = new MustacheEngine(mustacheTemplateLoader);
		NashornTemplate template = new NashornTemplate(mustacheEngine, reader);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.nashorn.NashornTemplate@%s{" +
						"template=\"<div>Hello {{name}}</div>\", " +
						"engine=%s" +
				"}";
		// @formatter:on

		assertThat(template).hasToString(String.format(
				expectedToString, hexIdentity(template), mustacheEngine
		));
	}
}
