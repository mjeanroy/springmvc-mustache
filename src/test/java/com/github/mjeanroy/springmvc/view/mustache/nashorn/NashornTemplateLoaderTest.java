/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 <mickael.jeanroy@gmail.com>
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NashornTemplateLoaderTest {

	@Mock
	private MustacheTemplateLoader mustacheTemplateLoader;

	private NashornTemplateLoader nashornTemplateLoader;

	@Before
	public void setUp() {
		nashornTemplateLoader = new NashornTemplateLoader(mustacheTemplateLoader);

		when(mustacheTemplateLoader.getTemplate(anyString())).thenAnswer(new Answer<Reader>() {
			@Override
			public Reader answer(InvocationOnMock invocation) throws Throwable {
				String location = invocation.getArguments()[0].toString();
				InputStream stream = NashornTemplateLoaderTest.class.getResourceAsStream(location);
				return new InputStreamReader(stream);
			}
		});
	}

	@Test
	public void it_should_read_template_source() throws IOException {
		String location = "/templates/foo.template.html";

		String template = nashornTemplateLoader.load(location);

		String expectedContent = "<div>Hello {{name}}</div>";
		assertThat(template).isNotNull().isEqualTo(expectedContent);
	}
}
