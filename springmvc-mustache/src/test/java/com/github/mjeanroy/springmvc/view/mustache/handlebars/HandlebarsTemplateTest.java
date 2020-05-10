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

package com.github.mjeanroy.springmvc.view.mustache.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsTemplateTest {

	@Test
	public void it_should_execute_template() {
		Writer writer = new StringWriter();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("foo", "bar");

		com.github.jknack.handlebars.Template template = createTemplate();
		HandlebarsTemplate handlebarsTemplate = new HandlebarsTemplate(template);
		handlebarsTemplate.execute(model, writer);

		assertThat(writer.toString()).isEqualTo(
				"foo :: bar"
		);
	}

	@Test
	public void it_should_implement_to_string() {
		com.github.jknack.handlebars.Template template = createTemplate();
		HandlebarsTemplate handlebarsTemplate = new HandlebarsTemplate(template);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.handlebars.HandlebarsTemplate@%s{" +
						"template=%s" +
				"}";
		// @formatter:on

		assertThat(handlebarsTemplate).hasToString(String.format(
				expectedToString, hexIdentity(handlebarsTemplate), template
		));
	}

	private static Template createTemplate() {
		try {
			return new Handlebars().compileInline("foo :: {{ foo }}");
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}
}
