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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.nashorn.MustacheEngine;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("deprecation")
public class MustacheEngineFactoryBeanTest {

	private MustacheTemplateLoader templateLoader;

	private MustacheEngineFactoryBean factoryBean;

	@Before
	public void setUp() {
		templateLoader = mock(MustacheTemplateLoader.class);
		factoryBean = new MustacheEngineFactoryBean(templateLoader);
	}

	@Test
	public void it_should_create_factory_bean_with_default_settings() throws Exception {
		String path = (String) readField(factoryBean, "path", true);
		MustacheTemplateLoader tmplLoader = (MustacheTemplateLoader) readField(factoryBean, "templateLoader", true);

		assertThat(path).isNull();
		assertThat(tmplLoader).isSameAs(templateLoader);
	}

	@Test
	public void it_should_create_factory_bean_as_singleton() throws Exception {
		assertThat(factoryBean.isSingleton()).isTrue();
	}

	@Test
	public void it_should_create_factory_bean_with_target_class() throws Exception {
		assertThat(factoryBean.getObjectType()).isEqualTo(MustacheEngine.class);
	}

	@Test
	public void it_should_create_target_object_with_default_settings() throws Exception {
		factoryBean.afterPropertiesSet();
		MustacheEngine mustacheEngine = factoryBean.getObject();
		assertThat(mustacheEngine).isNotNull();
	}

	@Test
	public void it_should_not_create_target_object_twice() throws Exception {
		factoryBean.afterPropertiesSet();

		MustacheEngine m1 = factoryBean.getObject();
		MustacheEngine m2 = factoryBean.getObject();

		assertThat(m1).isNotNull();
		assertThat(m2).isNotNull();
		assertThat(m1).isSameAs(m2);
	}
}
