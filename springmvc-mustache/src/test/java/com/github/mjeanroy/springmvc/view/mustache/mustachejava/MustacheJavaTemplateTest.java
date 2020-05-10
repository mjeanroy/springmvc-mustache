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

package com.github.mjeanroy.springmvc.view.mustache.mustachejava;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MustacheJavaTemplateTest {

	@Test
	public void it_should_execute_template() {
		Writer writer = new StringWriter();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("foo", "bar");

		Reader reader = new StringReader("foo :: {{ foo }}");
		Mustache mustache = new DefaultMustacheFactory().compile(reader, "foo");
		MustacheJavaTemplate mustacheJavaTemplate = new MustacheJavaTemplate(mustache);
		mustacheJavaTemplate.execute(model, writer);

		assertThat(writer.toString()).isEqualTo(
				"foo :: bar"
		);
	}

	@Test
	public void it_should_implement_to_string() {
		Mustache mustache = mock(Mustache.class, "MustacheJava");
		MustacheJavaTemplate mustacheJavaTemplate = new MustacheJavaTemplate(mustache);
		String identity = hexIdentity(mustacheJavaTemplate);

		// @formatter:off
		assertThat(mustacheJavaTemplate).hasToString(
				"com.github.mjeanroy.springmvc.view.mustache.mustachejava.MustacheJavaTemplate@" + identity + "{" +
						"mustache=MustacheJava" +
				"}"
		);
		// @formatter:on
	}
}
