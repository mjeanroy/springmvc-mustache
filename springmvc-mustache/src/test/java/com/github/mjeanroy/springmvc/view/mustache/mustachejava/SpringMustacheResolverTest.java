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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mustachejava.MustacheResolver;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.Reader;

import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.IOTestUtils.read;
import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.TestUtils.hexIdentity;
import static org.assertj.core.api.Assertions.assertThat;

class SpringMustacheResolverTest {

	@Test
	void it_should_resolve_template() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		String prefix = "/templates/";
		String suffix = ".template.html";

		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		MustacheResolver mustacheResolver = new SpringMustacheResolver(templateLoader);

		String name = "foo";

		Reader result = mustacheResolver.getReader(name);

		assertThat(result).isNotNull();
		assertThat(read(result)).isEqualTo("<div>Hello {{name}}</div>");
	}

	@Test
	void it_should_implement_to_string() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheResolver mustacheResolver = new SpringMustacheResolver(templateLoader);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheResolver@%s{" +
						"templateLoader=%s" +
				"}";
		// @formatter:on

		assertThat(mustacheResolver).hasToString(String.format(
				expectedToString, hexIdentity(mustacheResolver), templateLoader
		));
	}
}
