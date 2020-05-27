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

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MustacheConfigurationTest {

	private MustacheEngineConfiguration mustacheConfiguration;

	@Before
	public void setUp() {
		mustacheConfiguration = new MustacheEngineConfiguration();
	}

	@Test
	public void it_should_select_configuration_class() {
		MustacheProvider provider = MustacheProvider.JMUSTACHE;
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("provider", provider);

		AnnotationMetadata annotationMetadata = mock(AnnotationMetadata.class);
		when(annotationMetadata.getAnnotationAttributes(EnableMustache.class.getName())).thenReturn(attributes);

		String[] imports = mustacheConfiguration.selectImports(annotationMetadata);

		assertThat(imports).hasSize(1).containsExactly(
				provider.configurationClass()
		);
	}
}
