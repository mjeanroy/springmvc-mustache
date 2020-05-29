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

package com.github.mjeanroy.springmvc.mustache.sample.mustachejavaxml.it;

import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Rule;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexControllerTest {

	@ClassRule
	public static JettyServerJunit4Rule jetty = new JettyServerJunit4Rule();

	@Test
	public void it_should_render_mustache_template() {
		HttpResponse response = jetty.getClient().prepareGet("/").execute();
		assertThat(response.status()).isEqualTo(200);
		assertThat(response.body()).contains(
				"Hello, my name is John Doe"
		);
	}

	@Test
	public void it_should_render_mustache_template_using_model_and_mustache_view() {
		HttpResponse response = jetty.getClient().prepareGet("/jane").execute();
		assertThat(response.status()).isEqualTo(200);
		assertThat(response.body()).contains(
				"Hello, my name is Jane Doe"
		);
	}
}
