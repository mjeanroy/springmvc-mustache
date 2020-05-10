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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.Reader;

import static com.github.mjeanroy.springmvc.view.mustache.tests.IOTestUtils.read;
import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static org.assertj.core.api.Assertions.assertThat;

public class JMustacheTemplateLoaderTest {

	@Test
	public void it_should_load_template() {
		JMustacheTemplateLoader jMustacheTemplateLoader = jMustacheTemplateLoader();
		String name = "/templates/foo.template.html";

		Reader result = jMustacheTemplateLoader.getTemplate(name);

		assertThat(result).isNotNull();
		assertThat(read(result)).isEqualTo("<div>Hello {{name}}</div>");
	}

	@Test
	public void it_should_implement_to_string() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		JMustacheTemplateLoader jMustacheTemplateLoader = jMustacheTemplateLoader(templateLoader);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheTemplateLoader@%s{" +
						"loader=%s" +
				"}";
		// @formatter:on

		assertThat(jMustacheTemplateLoader).hasToString(String.format(
				expectedToString, hexIdentity(jMustacheTemplateLoader), templateLoader
		));
	}

	private static MustacheTemplateLoader mustacheTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return new DefaultTemplateLoader(resourceLoader);
	}

	private static JMustacheTemplateLoader jMustacheTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return jMustacheTemplateLoader(resourceLoader);
	}

	private static JMustacheTemplateLoader jMustacheTemplateLoader(ResourceLoader resourceLoader) {
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		return jMustacheTemplateLoader(templateLoader);
	}

	private static JMustacheTemplateLoader jMustacheTemplateLoader(MustacheTemplateLoader templateLoader) {
		return new JMustacheTemplateLoader(templateLoader);
	}
}
