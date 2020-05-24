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

package com.github.mjeanroy.springmvc.view.mustache.configuration.nashorn;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.nashorn.MustacheEngine;
import com.github.mjeanroy.springmvc.view.mustache.nashorn.NashornCompiler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
public class NashornConfigurationTest {

	private Environment environment;
	private NashornConfiguration nashornConfiguration;

	@Before
	public void setUp() {
		environment = new MockEnvironment();
		nashornConfiguration = new NashornConfiguration(environment);
	}

	@Test
	public void it_should_create_mustache_compiler() {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		MustacheEngine mustacheEngine = new MustacheEngine(templateLoader);

		MustacheCompiler mustacheCompiler = nashornConfiguration.mustacheCompiler(templateLoader, mustacheEngine);

		assertThat(mustacheCompiler).isExactlyInstanceOf(NashornCompiler.class);
	}

	@Test
	public void it_should_create_mustache_engine() throws Exception {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);

		MustacheEngineFactoryBean factoryBean = nashornConfiguration.mustacheEngine(templateLoader);
		factoryBean.afterPropertiesSet();
		MustacheEngine engine = factoryBean.getObject();

		assertThat(engine).isNotNull();
	}
}
