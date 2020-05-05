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
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsTemplateLoaderTest {

	private MustacheTemplateLoader mustacheTemplateLoader;

	private HandlebarsTemplateLoader handlebarsTemplateLoader;

	@Before
	public void setUp() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		String prefix = "/templates/";
		String suffix = ".template.html";

		mustacheTemplateLoader = new DefaultTemplateLoader(resourceLoader, prefix, suffix);
		handlebarsTemplateLoader = new HandlebarsTemplateLoader(mustacheTemplateLoader);
	}

	@Test
	public void it_should_read_template_source() throws IOException {
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
		String location = "/templates/foo.template.html";
		String templateName = "foo";

		String result = handlebarsTemplateLoader.resolve(templateName);

		assertThat(result).isEqualTo(location);
	}

	@Test
	public void it_should_get_prefix() {
		String result = handlebarsTemplateLoader.getPrefix();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo("/templates/");
	}

	@Test
	public void it_should_get_suffix() {
		String result = handlebarsTemplateLoader.getSuffix();
		assertThat(result).isNotNull().isNotEmpty().isEqualTo(".template.html");
	}

	@Test
	public void it_should_set_prefix() {
		String prefix = "foo";
		handlebarsTemplateLoader.setPrefix(prefix);
		assertThat(mustacheTemplateLoader.getPrefix()).isEqualTo(prefix);
	}

	@Test
	public void it_should_set_suffix() {
		String suffix = "foo";
		handlebarsTemplateLoader.setSuffix(suffix);
		assertThat(mustacheTemplateLoader.getSuffix()).isEqualTo(suffix);
	}
}
