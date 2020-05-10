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

import com.github.jknack.handlebars.io.TemplateSource;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsTemplateLoaderTest {

	@Test
	public void it_should_read_template_source() throws IOException {
		HandlebarsTemplateLoader handlebarsTemplateLoader = handlebarsTemplateLoader();
		String location = "/templates/foo.template.html";

		TemplateSource source = handlebarsTemplateLoader.sourceAt(location);

		String expectedContent = "<div>Hello {{name}}</div>";
		assertThat(source).isNotNull();
		assertThat(source.filename()).isNotNull().isNotEmpty().isEqualTo(location);
		assertThat(source.lastModified()).isNotNull().isEqualTo(expectedContent.hashCode());
		assertThat(source.content()).isNotNull().isNotEmpty().isEqualTo(expectedContent);
	}

	@Test
	public void it_should_resolve_template_location() {
		HandlebarsTemplateLoader handlebarsTemplateLoader = handlebarsTemplateLoader();
		String location = "/templates/foo.template.html";
		String templateName = "foo";

		String result = handlebarsTemplateLoader.resolve(templateName);

		assertThat(result).isEqualTo(location);
	}

	@Test
	public void it_should_get_prefix() {
		HandlebarsTemplateLoader handlebarsTemplateLoader = handlebarsTemplateLoader();
		String result = handlebarsTemplateLoader.getPrefix();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo("/templates/");
	}

	@Test
	public void it_should_get_suffix() {
		HandlebarsTemplateLoader handlebarsTemplateLoader = handlebarsTemplateLoader();
		String result = handlebarsTemplateLoader.getSuffix();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(".template.html");
	}

	@Test
	public void it_should_set_prefix() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		HandlebarsTemplateLoader handlebarsTemplateLoader = new HandlebarsTemplateLoader(mustacheTemplateLoader);

		String prefix = "/templates/";
		handlebarsTemplateLoader.setPrefix(prefix);

		assertThat(mustacheTemplateLoader.getPrefix()).isEqualTo(prefix);
	}

	@Test
	public void it_should_set_suffix() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		HandlebarsTemplateLoader handlebarsTemplateLoader = new HandlebarsTemplateLoader(mustacheTemplateLoader);

		String suffix = ".template.html";
		handlebarsTemplateLoader.setSuffix(suffix);

		assertThat(mustacheTemplateLoader.getSuffix()).isEqualTo(suffix);
	}

	@Test
	public void it_should_implement_to_string() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader);
		HandlebarsTemplateLoader handlebarsTemplateLoader = new HandlebarsTemplateLoader(mustacheTemplateLoader);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.handlebars.HandlebarsTemplateLoader@%s{" +
						"loader=%s" +
				"}";
		// @formatter:on

		assertThat(handlebarsTemplateLoader).hasToString(String.format(
				expectedToString, hexIdentity(handlebarsTemplateLoader), mustacheTemplateLoader
		));
	}

	private static HandlebarsTemplateLoader handlebarsTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return handlebarsTemplateLoader(resourceLoader);
	}

	private static HandlebarsTemplateLoader handlebarsTemplateLoader(ResourceLoader resourceLoader) {
		String prefix = "/templates/";
		String suffix = ".template.html";
		MustacheTemplateLoader mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		return new HandlebarsTemplateLoader(mustacheTemplateLoader);
	}
}
