/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HandlebarsTemplateLoaderTest {

	@Mock
	private MustacheTemplateLoader mustacheTemplateLoader;

	private HandlebarsTemplateLoader handlebarsTemplateLoader;

	@Before
	public void setUp() {
		handlebarsTemplateLoader = new HandlebarsTemplateLoader(mustacheTemplateLoader);

		when(mustacheTemplateLoader.getTemplate(anyString())).thenAnswer(new Answer<Reader>() {
			@Override
			public Reader answer(InvocationOnMock invocation) throws Throwable {
				String location = invocation.getArguments()[0].toString();
				InputStream stream = HandlebarsTemplateLoaderTest.class.getResourceAsStream(location);
				return new InputStreamReader(stream);
			}
		});
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
		when(mustacheTemplateLoader.resolve(templateName)).thenReturn(location);

		String result = handlebarsTemplateLoader.resolve(templateName);

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(location);
		verify(mustacheTemplateLoader).resolve(templateName);
	}

	@Test
	public void it_should_get_prefix() {
		String prefix = "/templates/";
		when(mustacheTemplateLoader.getPrefix()).thenReturn(prefix);

		String result = handlebarsTemplateLoader.getPrefix();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(prefix);
		verify(mustacheTemplateLoader).getPrefix();
	}

	@Test
	public void it_should_get_suffix() {
		String suffix = ".template.html";
		when(mustacheTemplateLoader.getSuffix()).thenReturn(suffix);

		String result = handlebarsTemplateLoader.getSuffix();

		assertThat(result).isNotNull().isNotEmpty().isEqualTo(suffix);
		verify(mustacheTemplateLoader).getSuffix();
	}

	@Test
	public void it_should_set_prefix() {
		String prefix = "foo";
		handlebarsTemplateLoader.setPrefix(prefix);
		verify(mustacheTemplateLoader).setPrefix(prefix);
	}

	@Test
	public void it_should_set_suffix() {
		String suffix = "foo";
		handlebarsTemplateLoader.setSuffix(suffix);
		verify(mustacheTemplateLoader).setSuffix(suffix);
	}
}
