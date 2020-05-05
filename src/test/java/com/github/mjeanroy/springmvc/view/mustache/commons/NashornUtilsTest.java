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

import com.github.mjeanroy.junit4.runif.RunIf;
import com.github.mjeanroy.junit4.runif.RunIfRunner;
import com.github.mjeanroy.junit4.runif.conditions.AtLeastJava8Condition;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.NashornException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.script.ScriptEngine;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

@RunWith(RunIfRunner.class)
@RunIf(AtLeastJava8Condition.class)
public class NashornUtilsTest {

	@Rule
	public ExpectedException thrown = none();

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
		thrown.expect(NashornException.class);

		NashornUtils.getEngine(singletonList(
				getClass().getResourceAsStream("/scripts/test-with-error.js")
		));
	}
}
