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

package com.github.mjeanroy.springmvc.view.mustache.logging;

import com.github.mjeanroy.junit4.runif.RunIf;
import com.github.mjeanroy.junit4.runif.RunIfRunner;
import com.github.mjeanroy.junit4.runif.conditions.AtLeastJava8Condition;
import org.junit.runner.RunWith;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;

@RunWith(RunIfRunner.class)
@RunIf(AtLeastJava8Condition.class)
public class Log4j2LoggerTest extends AbstractLoggerTest {

	@Override
	Logger createLogger() {
		Log4j2Logger log = new Log4j2Logger(getClass());
		doUpdateLevel(log, "TRACE");
		return log;
	}

	@Override
	void updateLevel(String level) {
		doUpdateLevel(log, level);
	}

	private static void doUpdateLevel(Logger log, String level) {
		ch.qos.logback.classic.Logger logback = readField(readField(log, "log"), "logger");
		logback.setLevel(ch.qos.logback.classic.Level.valueOf(level));
	}
}
