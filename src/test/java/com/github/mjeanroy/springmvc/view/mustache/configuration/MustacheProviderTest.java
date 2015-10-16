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

package com.github.mjeanroy.springmvc.view.mustache.configuration;

import com.github.jknack.handlebars.Handlebars;
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.ClassUtils;
import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.handlebars.HandlebarsCompiler;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.MustacheJavaCompiler;
import com.samskivert.mustache.Mustache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClassUtils.class)
public class MustacheProviderTest {

	private ApplicationContext applicationContext;

	@Before
	public void setUp() {
		applicationContext = mock(ApplicationContext.class);

		MustacheTemplateLoader templateLoader = mock(MustacheTemplateLoader.class);
		when(applicationContext.getBean(MustacheTemplateLoader.class)).thenReturn(templateLoader);

		Mustache.Compiler jmustache = mock(Mustache.Compiler.class);
		when(applicationContext.getBean(Mustache.Compiler.class)).thenReturn(jmustache);

		Handlebars handlebars = mock(Handlebars.class);
		when(applicationContext.getBean(Handlebars.class)).thenReturn(handlebars);

		mockStatic(ClassUtils.class);
	}

	@Test
	public void it_should_get_configuration_class_for_jmustache() {
		assertThat(MustacheProvider.JMUSTACHE.configuration()).isEqualTo(JMustacheConfiguration.class);
	}

	@Test
	public void it_should_check_if_jmustache_is_available() {
		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(false);
		assertThat(MustacheProvider.JMUSTACHE.isAvailable()).isFalse();

		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(true);
		assertThat(MustacheProvider.JMUSTACHE.isAvailable()).isTrue();
	}

	@Test
	public void it_should_get_jmustache_compiler() {
		MustacheCompiler mustacheCompiler = MustacheProvider.JMUSTACHE.instantiate(applicationContext);

		verify(applicationContext).getBean(MustacheTemplateLoader.class);
		verify(applicationContext).getBean(Mustache.Compiler.class);

		assertThat(mustacheCompiler)
				.isNotNull()
				.isExactlyInstanceOf(JMustacheCompiler.class);
	}

	@Test
	public void it_should_get_configuration_class_for_handlebars() {
		assertThat(MustacheProvider.HANDLEBARS.configuration()).isEqualTo(HandlebarsConfiguration.class);
	}

	@Test
	public void it_should_check_if_handlebars_is_available() {
		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(false);
		assertThat(MustacheProvider.HANDLEBARS.isAvailable()).isFalse();

		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(true);
		assertThat(MustacheProvider.HANDLEBARS.isAvailable()).isTrue();
	}

	@Test
	public void it_should_get_handlebars_compiler() {
		MustacheCompiler mustacheCompiler = MustacheProvider.HANDLEBARS.instantiate(applicationContext);

		verify(applicationContext).getBean(MustacheTemplateLoader.class);
		verify(applicationContext).getBean(Handlebars.class);

		assertThat(mustacheCompiler)
				.isNotNull()
				.isExactlyInstanceOf(HandlebarsCompiler.class);
	}

	@Test
	public void it_should_get_configuration_class_for_mustache_java() {
		assertThat(MustacheProvider.MUSTACHE_JAVA.configuration()).isEqualTo(MustacheJavaConfiguration.class);
	}

	@Test
	public void it_should_check_if_mustache_java_is_available() {
		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(false);
		assertThat(MustacheProvider.MUSTACHE_JAVA.isAvailable()).isFalse();

		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(true);
		assertThat(MustacheProvider.MUSTACHE_JAVA.isAvailable()).isTrue();
	}

	@Test
	public void it_should_get_mustache_java_compiler() {
		MustacheCompiler mustacheCompiler = MustacheProvider.MUSTACHE_JAVA.instantiate(applicationContext);

		verify(applicationContext).getBean(MustacheTemplateLoader.class);

		assertThat(mustacheCompiler)
				.isNotNull()
				.isExactlyInstanceOf(MustacheJavaCompiler.class);
	}

	@Test
	public void it_should_get_jmustache_by_default_with_auto_configuration() {
		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(true);
		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(true);
		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(true);

		Class klass = MustacheProvider.AUTO.configuration();
		assertThat(klass)
				.isNotNull()
				.isEqualTo(JMustacheConfiguration.class);
	}

	@Test
	public void it_should_get_handlebars_with_auto_configuration() {
		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(false);
		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(true);
		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(true);

		Class klass = MustacheProvider.AUTO.configuration();
		assertThat(klass)
				.isNotNull()
				.isEqualTo(HandlebarsConfiguration.class);
	}

	@Test
	public void it_should_get_mustachejava_with_auto_configuration() {
		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(false);
		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(false);
		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(true);

		Class klass = MustacheProvider.AUTO.configuration();
		assertThat(klass)
				.isNotNull()
				.isEqualTo(MustacheJavaConfiguration.class);
	}

	@Test
	public void it_should_get_jmustache_compiler_with_auto_configuration() {
		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(true);
		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(true);
		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(true);

		MustacheCompiler mustacheCompiler = MustacheProvider.AUTO.instantiate(applicationContext);
		assertThat(mustacheCompiler)
				.isNotNull()
				.isExactlyInstanceOf(JMustacheCompiler.class);
	}

	@Test
	public void it_should_get_handlebars_compiler_with_auto_configuration() {
		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(false);
		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(true);
		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(true);

		MustacheCompiler mustacheCompiler = MustacheProvider.AUTO.instantiate(applicationContext);
		assertThat(mustacheCompiler)
				.isNotNull()
				.isExactlyInstanceOf(HandlebarsCompiler.class);
	}

	@Test
	public void it_should_get_mustachejava_compiler_with_auto_configuration() {
		PowerMockito.when(ClassUtils.isPresent("com.samskivert.mustache.Mustache")).thenReturn(false);
		PowerMockito.when(ClassUtils.isPresent("com.github.jknack.handlebars.Handlebars")).thenReturn(false);
		PowerMockito.when(ClassUtils.isPresent("com.github.mustachejava.MustacheFactory")).thenReturn(true);

		MustacheCompiler mustacheCompiler = MustacheProvider.AUTO.instantiate(applicationContext);
		assertThat(mustacheCompiler)
				.isNotNull()
				.isExactlyInstanceOf(MustacheJavaCompiler.class);
	}
}
