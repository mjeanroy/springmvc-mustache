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

package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.samskivert.mustache.Escapers;
import com.samskivert.mustache.Mustache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static com.github.mjeanroy.springmvc.view.mustache.tests.utils.ReflectionTestUtils.readField;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JMustacheCompilerFactoryBeanTest {

	private JMustacheCompilerFactoryBean factoryBean;

	@BeforeEach
	void setUp() {
		factoryBean = new JMustacheCompilerFactoryBean();
	}

	@Test
	void it_should_not_create_target_object_twice() throws Exception {
		factoryBean.afterPropertiesSet();

		Mustache.Compiler c1 = factoryBean.getObject();
		Mustache.Compiler c2 = factoryBean.getObject();

		assertThat(c1).isNotNull();
		assertThat(c2).isNotNull();
		assertThat(c1).isSameAs(c2);
	}

	@Test
	void it_should_create_factory_bean_with_default_settings() {
		String nullValue = readField(factoryBean, "nullValue");
		String defaultValue = readField(factoryBean, "defaultValue");
		boolean emptyStringIsFalse = readField(factoryBean, "emptyStringIsFalse");
		boolean zeroIsFalse = readField(factoryBean, "zeroIsFalse");
		boolean escapeHTML = readField(factoryBean, "escapeHTML");
		boolean strictSections = readField(factoryBean, "strictSections");
		boolean standardsMode = readField(factoryBean, "standardsMode");

		assertThat(nullValue).isEqualTo("");
		assertThat(defaultValue).isEqualTo("");
		assertThat(emptyStringIsFalse).isTrue();
		assertThat(zeroIsFalse).isTrue();
		assertThat(escapeHTML).isTrue();
		assertThat(strictSections).isFalse();
		assertThat(standardsMode).isFalse();
	}

	@Test
	void it_should_create_factory_bean_as_singleton() {
		assertThat(factoryBean.isSingleton()).isTrue();
	}

	@Test
	void it_should_create_factory_bean_with_target_class() {
		assertThat(factoryBean.getObjectType()).isEqualTo(Mustache.Compiler.class);
	}

	@Test
	void it_should_create_target_object_with_default_settings() throws Exception {
		factoryBean.afterPropertiesSet();

		Mustache.Compiler compiler = factoryBean.getObject();

		assertThat(compiler).isNotNull();
		assertThat(compiler.nullValue).isEqualTo("");
		assertThat(compiler.emptyStringIsFalse).isTrue();
		assertThat(compiler.zeroIsFalse).isTrue();
		assertThat(compiler.escaper).isEqualTo(Escapers.HTML);
		assertThat(compiler.standardsMode).isFalse();
		assertThat(compiler.strictSections).isFalse();
	}

	@Test
	void it_should_create_target_object_with_empty_string_is_false_property() throws Exception {
		factoryBean.setEmptyStringIsFalse(false);
		factoryBean.afterPropertiesSet();
		Mustache.Compiler compiler = factoryBean.getObject();
		assertThat(compiler.emptyStringIsFalse).isFalse();
	}

	@Test
	void it_should_create_target_object_with_zero_is_false_property() throws Exception {
		factoryBean.setZeroIsFalse(false);
		factoryBean.afterPropertiesSet();
		Mustache.Compiler compiler = factoryBean.getObject();
		assertThat(compiler.zeroIsFalse).isFalse();
	}

	@Test
	void it_should_create_target_object_with_escape_HTML_property() throws Exception {
		factoryBean.setEscapeHTML(false);
		factoryBean.afterPropertiesSet();
		Mustache.Compiler compiler = factoryBean.getObject();
		assertThat(compiler.escaper).isEqualTo(Escapers.NONE);
	}

	@Test
	void it_should_create_target_object_with_standards_mode_property() throws Exception {
		factoryBean.setStandardsMode(true);
		factoryBean.afterPropertiesSet();
		Mustache.Compiler compiler = factoryBean.getObject();
		assertThat(compiler.standardsMode).isTrue();
	}

	@Test
	void it_should_create_target_object_with_strict_section_property() throws Exception {
		factoryBean.setStrictSections(true);
		factoryBean.afterPropertiesSet();
		Mustache.Compiler compiler = factoryBean.getObject();
		assertThat(compiler.strictSections).isTrue();
	}

	@Test
	void it_should_create_target_object_with_null_value_property() throws Exception {
		factoryBean.setDefaultValue("null");
		factoryBean.setNullValue("null");
		factoryBean.afterPropertiesSet();
		Mustache.Compiler compiler = factoryBean.getObject();
		assertThat(compiler.nullValue).isEqualTo("null");
	}

	@Test
	void it_should_create_target_object_with_default_value_property() throws Exception {
		factoryBean.setDefaultValue("default");
		factoryBean.afterPropertiesSet();
		Mustache.Compiler compiler = factoryBean.getObject();
		assertThat(compiler.nullValue).isEqualTo("default");
	}

	@Test
	void it_should_customize_jmustache_compiler() throws Exception {
		JMustacheCustomizer c1 = newJMustacheCustomizer();
		JMustacheCustomizer c2 = newJMustacheCustomizer();
		List<JMustacheCustomizer> customizers = asList(c1, c2);

		factoryBean.setCustomizers(customizers);
		factoryBean.afterPropertiesSet();

		Mustache.Compiler compiler = factoryBean.getObject();

		InOrder inOrder = inOrder(c1, c2);
		inOrder.verify(c1).customize(compiler);
		inOrder.verify(c2).customize(compiler);
	}

	private static JMustacheCustomizer newJMustacheCustomizer() {
		JMustacheCustomizer customizer = mock(JMustacheCustomizer.class);
		when(customizer.customize(any(Mustache.Compiler.class))).thenAnswer((invocation) ->
				invocation.getArgument(0)
		);

		return customizer;
	}
}
