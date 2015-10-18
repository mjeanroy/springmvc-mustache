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

package com.github.mjeanroy.springmvc.view.mustache.commons;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheIOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class IOUtilsTest {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_read_input() {
		InputStream stream = getClass().getResourceAsStream("/templates/foo.template.html");
		Reader reader = new InputStreamReader(stream);

		String content = IOUtils.read(reader);

		assertThat(content)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo("<div>Hello {{name}}</div>");
	}

	@Test
	public void it_should_read_multiline_input() {
		InputStream stream = getClass().getResourceAsStream("/templates/composite.template.html");
		Reader reader = new InputStreamReader(stream);

		String content = IOUtils.read(reader);

		assertThat(content)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo("" +
						"<div>" + LINE_SEPARATOR +
						"	{{> /templates/foo.template.html}}" + LINE_SEPARATOR +
						"</div>"
				);
	}

	@Test
	public void it_should_get_stream() {
		String fileName = "/templates/foo.template.html";
		InputStream stream = IOUtils.getStream(fileName);
		assertThat(stream)
				.isNotNull()
				.hasSameContentAs(getClass().getResourceAsStream(fileName));
	}

	@Test
	public void it_should_fail_if_stream_does_not_exist() {
		thrown.expect(MustacheIOException.class);
		thrown.expectMessage("I/O Error with foo.bar");

		String fileName = "foo.bar";
		IOUtils.getStream(fileName);
	}

	@Test
	public void it_should_get_first_available_stream() {
		List<String> fileNames = asList(
				"/templates/fake.template.html",
				"/templates/zero.template.html",
				"/templates/composite.template.html"
		);

		InputStream stream = IOUtils.getFirstAvailableStream(fileNames);
		assertThat(stream)
				.isNotNull()
				.hasSameContentAs(getClass().getResourceAsStream(fileNames.get(1)));
	}

	@Test
	public void it_should_fail_when_no_stream_is_available() {
		thrown.expect(MustacheIOException.class);
		thrown.expectMessage("Unable to locate one of: [/templates/fake1.template.html, /templates/fake1.template.html]");

		List<String> fileNames = asList(
				"/templates/fake1.template.html",
				"/templates/fake1.template.html"
		);

		IOUtils.getFirstAvailableStream(fileNames);
	}
}
