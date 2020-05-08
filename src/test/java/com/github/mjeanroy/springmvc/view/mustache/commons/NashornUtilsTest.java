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

package com.github.mjeanroy.springmvc.view.mustache.commons;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import javax.script.ScriptEngine;
import java.io.InputStream;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NashornUtilsTest {

	@Test
	public void it_should_get_nashorn_engine() {
		ScriptEngine engine = NashornUtils.getEngine();
		assertThat(engine.getFactory().getEngineName()).containsIgnoringCase("nashorn");
	}

	@Test
	public void it_should_get_nashorn_engine_and_evaluate_scripts() {
		ScriptEngine engine = NashornUtils.getEngine(singletonList(
				getClass().getResourceAsStream("/scripts/test-constant.js")
		));

		assertThat(engine.getFactory().getEngineName()).containsIgnoringCase("nashorn");

		String result = (String) engine.get("HELLO_WORLD");
		assertThat(result).isEqualTo("Hello World");
	}

	@Test
	public void it_should_catch_script_exception() {
		final List<InputStream> scripts = singletonList(
				getClass().getResourceAsStream("/scripts/test-with-error.js")
		);

		final ThrowingCallable getEngine = new ThrowingCallable() {
			@Override
			public void call() {
				NashornUtils.getEngine(scripts);
			}
		};

		assertThatThrownBy(getEngine).isInstanceOf(NashornException.class);
	}
}
