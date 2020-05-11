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

import org.apache.commons.logging.impl.SLF4JLocationAwareLog;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;

public class CommonsLoggingLoggerTest extends AbstractLoggerTest {

	@Override
	Logger createLogger() {
		CommonsLoggingLogger log = new CommonsLoggingLogger(getClass());
		doUpdateLevel(log, "TRACE");
		return log;
	}

	@Override
	void updateLevel(String level) {
		doUpdateLevel(log, level);
	}

	private static void doUpdateLevel(Logger log, String level) {
		// Use jcl-over-slf4
		SLF4JLocationAwareLog slf4jLogger = readField(log, "log");
		ch.qos.logback.classic.Logger internalLog = readField(slf4jLogger, "logger");
		internalLog.setLevel(ch.qos.logback.classic.Level.valueOf(level));
	}
}
