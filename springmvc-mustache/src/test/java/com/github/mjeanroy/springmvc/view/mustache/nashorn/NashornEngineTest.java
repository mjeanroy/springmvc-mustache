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

package com.github.mjeanroy.springmvc.view.mustache.nashorn;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.script.ScriptEngine;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("deprecation")
public class NashornEngineTest {

	@Test
	public void it_should_create_default_engine() {
		MustacheTemplateLoader templateLoader = defaultTemplateLoader();
		MustacheEngine engine = new MustacheEngine(templateLoader);
		NashornPartialsObject partials = readField(engine, "partials");

		assertThat(partials).isNotNull();
		assertThat(readField(partials, "templateLoader", MustacheTemplateLoader.class)).isSameAs(templateLoader);
		assertThat(readField(engine, "engine", ScriptEngine.class)).isNotNull();
	}

	@Test
	public void it_should_create_with_given_scripts() {
		String script = "/META-INF/resources/webjars/mustache/**/mustache.js";
		MustacheTemplateLoader templateLoader = defaultTemplateLoader();
		MustacheEngine engine = new MustacheEngine(templateLoader, script);

		NashornPartialsObject partials = readField(engine, "partials");
		assertThat(partials).isNotNull();
		assertThat(readField(partials, "templateLoader", MustacheTemplateLoader.class)).isSameAs(templateLoader);
		assertThat(readField(engine, "engine", ScriptEngine.class)).isNotNull();
	}

	@Test
	public void it_should_create_with_given_stream() {
		InputStream stream = getClass().getResourceAsStream("/META-INF/resources/webjars/mustache/2.3.2/mustache.js");
		MustacheTemplateLoader templateLoader = defaultTemplateLoader();
		MustacheEngine engine = new MustacheEngine(templateLoader, stream);
		NashornPartialsObject partials = readField(engine, "partials");

		assertThat(partials).isNotNull();
		assertThat(readField(partials, "templateLoader", MustacheTemplateLoader.class)).isSameAs(templateLoader);
		assertThat(readField(engine, "engine", ScriptEngine.class)).isNotNull();
	}

	@Test
	public void it_should_execute_mustache() {
		InputStream stream = getClass().getResourceAsStream("/META-INF/resources/webjars/mustache/2.3.2/mustache.js");
		MustacheTemplateLoader templateLoader = defaultTemplateLoader();
		MustacheEngine engine = new MustacheEngine(templateLoader, stream);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "foo");

		String result = engine.render("<div>Hello {{name}}</div>", map);
		assertThat(result).isEqualTo("<div>Hello foo</div>");
	}

	@Test
	public void it_should_execute_mustache_and_catch_script_error() {
		final InputStream stream = getClass().getResourceAsStream("/scripts/render-with-error.js");
		final MustacheTemplateLoader templateLoader = defaultTemplateLoader();
		final MustacheEngine engine = new MustacheEngine(templateLoader, stream);
		final ThrowingCallable render = new ThrowingCallable() {
			@Override
			public void call() {
				engine.render("<div>Hello {{name}}</div>", new HashMap<String, Object>());
			}
		};

		assertThatThrownBy(render).isInstanceOf(NashornException.class);
	}

	@Test
	public void it_should_execute_mustache_and_catch_no_method_error() {
		final InputStream stream = getClass().getResourceAsStream("/scripts/test-constant.js");
		final MustacheTemplateLoader templateLoader = defaultTemplateLoader();
		final MustacheEngine engine = new MustacheEngine(templateLoader, stream);
		final ThrowingCallable render = new ThrowingCallable() {
			@Override
			public void call() {
				engine.render("<div>Hello {{name}}</div>", new HashMap<String, Object>());
			}
		};

		assertThatThrownBy(render).isInstanceOf(NashornException.class);
	}

	@Test
	public void it_should_implement_to_string() {
		String script = "/META-INF/resources/webjars/mustache/**/mustache.js";
		MustacheTemplateLoader templateLoader = defaultTemplateLoader();
		MustacheEngine engine = new MustacheEngine(templateLoader, script);

		ScriptEngine scriptEngine = readField(engine, "engine");
		NashornPartialsObject partials = readField(engine, "partials");

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.nashorn.MustacheEngine@%s{" +
						"engine=%s, " +
						"partials=%s" +
				"}";
		// @formatter:on

		assertThat(engine).hasToString(String.format(
				expectedToString, hexIdentity(engine), scriptEngine, partials
		));
	}

	@Test
	public void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(MustacheEngine.class).verify();
	}

	private static DefaultTemplateLoader defaultTemplateLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return defaultTemplateLoader(resourceLoader);
	}

	private static DefaultTemplateLoader defaultTemplateLoader(ResourceLoader resourceLoader) {
		return new DefaultTemplateLoader(resourceLoader);
	}
}
