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

package com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars;

import com.github.jknack.handlebars.Handlebars;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsFactoryBeanTest {

	private HandlebarsFactoryBean factoryBean;

	@Before
	public void setUp() {
		factoryBean = new HandlebarsFactoryBean();
	}

	@Test
	public void it_should_create_factory_bean_as_singleton() throws Exception {
		assertThat(factoryBean.isSingleton()).isTrue();
	}

	@Test
	public void it_should_create_factory_bean_with_target_class() throws Exception {
		assertThat(factoryBean.getObjectType()).isEqualTo(Handlebars.class);
	}

	@Test
	public void it_should_create_target_object_with_default_settings() throws Exception {
		factoryBean.afterPropertiesSet();

		Handlebars handlebars = factoryBean.getObject();

		assertThat(handlebars).isNotNull();
	}

	@Test
	public void it_should_not_create_target_object_twice() throws Exception {
		factoryBean.afterPropertiesSet();

		Handlebars h1 = factoryBean.getObject();
		Handlebars h2 = factoryBean.getObject();

		assertThat(h1).isNotNull();
		assertThat(h2).isNotNull();
		assertThat(h1).isSameAs(h2);
	}
}
