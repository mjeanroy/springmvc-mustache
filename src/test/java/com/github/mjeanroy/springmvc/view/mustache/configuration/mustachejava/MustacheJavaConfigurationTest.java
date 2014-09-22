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

package com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.Environment;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver;

@RunWith(MockitoJUnitRunner.class)
public class MustacheJavaConfigurationTest {

	@Mock
	private Environment environment;

	@InjectMocks
	private MustacheJavaConfiguration mustacheJavaConfiguration;

	@Before
	public void setUp() {
		when(environment.getProperty(anyString(), anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[1];
			}
		});
	}

	@Test
	public void it_should_instantiate_template_loader() {
		MustacheTemplateLoader templateLoader = mustacheJavaConfiguration.mustacheTemplateLoader();
		assertThat(templateLoader).isNotNull();
	}

	@Test
	public void it_should_instantiate_mustache_compiler() {
		MustacheCompiler mustacheCompiler = mustacheJavaConfiguration.mustacheCompiler();
		assertThat(mustacheCompiler).isNotNull();
	}

	@Test
	public void it_should_instantiate_mustache_view_resolver() {
		MustacheViewResolver mustacheViewResolver = mustacheJavaConfiguration.mustacheViewResolver();
		assertThat(mustacheViewResolver).isNotNull();
	}

	@Test
	public void it_should_parse_layout_mappings() {
		String admin1 = "admin1";
		String admin2 = "admin2";
		String secure = "secure";

		String mappings = admin1 + ":" + secure + ";" + admin2 + ":" + secure;
		when(environment.getProperty("mustache.layoutMappings", "")).thenReturn(mappings);

		Map<String, String> map = mustacheJavaConfiguration.getLayoutMappings();

		assertThat(map).isNotNull().isNotEmpty().hasSize(2)
				.containsOnly(
						entry(admin1, secure),
						entry(admin2, secure)
				);
	}

	@Test
	public void it_should_parse_view_names() {
		String n1 = "*.template.html";
		String n2 = "*.mustache";
		String viewNames = n1 + ", " + n2;
		when(environment.getProperty("mustache.viewNames", "*")).thenReturn(viewNames);

		String[] names = mustacheJavaConfiguration.getViewNames();

		assertThat(names).isNotNull().isNotEmpty().hasSize(2).containsOnly(n1, n2);
	}
}
