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

package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static org.assertj.core.api.Assertions.assertThat;

public class JMustacheTemplateTest {

	@Test
	public void it_should_execute_template() {
		Writer writer = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("foo", "bar");

		Template template = Mustache.compiler().compile("foo :: {{ foo }}");
		JMustacheTemplate jMustacheTemplate = new JMustacheTemplate(template);
		jMustacheTemplate.execute(model, writer);

		assertThat(writer.toString()).isEqualTo("foo :: bar");
	}

	@Test
	public void it_should_implement_to_string() {
		Template template = Mustache.compiler().compile("foo :: {{ foo }}");
		JMustacheTemplate jMustacheTemplate = new JMustacheTemplate(template);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheTemplate@%s{" +
						"template=%s" +
				"}";
		// @formatter:on

		assertThat(jMustacheTemplate).hasToString(String.format(
				expectedToString, hexIdentity(jMustacheTemplate), template
		));
	}

	@Test
	public void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(JMustacheTemplate.class).verify();
	}
}
