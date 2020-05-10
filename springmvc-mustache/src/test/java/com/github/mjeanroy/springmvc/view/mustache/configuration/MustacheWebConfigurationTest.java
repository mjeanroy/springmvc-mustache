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

package com.github.mjeanroy.springmvc.view.mustache.configuration;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;

public class MustacheWebConfigurationTest {

	private MockEnvironment environment;
	private MustacheWebConfiguration mustacheWebConfiguration;

	@Before
	public void setUp() {
		environment = new MockEnvironment();
		mustacheWebConfiguration = new MustacheWebConfiguration(environment, mock(MustacheCompiler.class));
	}

	@Test
	public void it_should_instantiate_mustache_view_resolver() {
		MustacheViewResolver mustacheViewResolver = mustacheWebConfiguration.mustacheViewResolver();
		assertThat(mustacheViewResolver).isNotNull();
	}

	@Test
	public void it_should_parse_layout_mappings() {
		String admin1 = "admin1";
		String admin2 = "admin2";
		String secure = "secure";
		String mappings = admin1 + ":" + secure + ";" + admin2 + ":" + secure;

		environment.setProperty("mustache.layoutMappings", mappings);

		Map<String, String> map = mustacheWebConfiguration.getLayoutMappings();

		assertThat(map).hasSize(2).containsOnly(
				entry(admin1, secure),
				entry(admin2, secure)
		);
	}

	@Test
	public void it_should_parse_layout_mappings_and_skip_empty_parts() {
		String admin1 = "admin1";
		String admin2 = "admin2";
		String secure = "secure";
		String mappings = admin1 + ":" + secure + ";;" + admin2 + ":" + secure;

		environment.setProperty("mustache.layoutMappings", mappings);

		Map<String, String> map = mustacheWebConfiguration.getLayoutMappings();

		assertThat(map).hasSize(2).containsOnly(
				entry(admin1, secure),
				entry(admin2, secure)
		);
	}

	@Test
	public void it_should_parse_layout_mappings_and_fail_with_invalid_format() {
		String admin1 = "admin1";
		String mappings = admin1 + ":;";

		environment.setProperty("mustache.layoutMappings", mappings);

		ThrowingCallable getLayoutMappings = new ThrowingCallable() {
			@Override
			public void call() {
				mustacheWebConfiguration.getLayoutMappings();
			}
		};

		assertThatThrownBy(getLayoutMappings)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Mapping must use [viewName]:[layout] format!");
	}

	@Test
	public void it_should_parse_view_names() {
		String n1 = "*.template.html";
		String n2 = "*.mustache";
		String viewNames = n1 + ", " + n2;

		environment.setProperty("mustache.viewNames", viewNames);

		String[] names = mustacheWebConfiguration.getViewNames();

		assertThat(names).hasSize(2).containsOnly(
				n1,
				n2
		);
	}
}
