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

import org.apache.commons.logging.impl.SimpleLog;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;

public class CommonsLoggingLoggerTest extends AbstractLoggerTest {

	@Override
	Logger createLogger() {
		CommonsLoggingLogger log = new CommonsLoggingLogger(getClass());

		SimpleLog internalLog = readField(log, "log");
		internalLog.setLevel(Level.TRACE.level);

		return log;
	}

	@Override
	void updateLevel(String level) {
		SimpleLog internalLog = readField(log, "log");
		Level lvl = Level.valueOf(level);
		internalLog.setLevel(lvl.level);
	}

	private enum Level {
		TRACE(SimpleLog.LOG_LEVEL_TRACE),
		DEBUG(SimpleLog.LOG_LEVEL_DEBUG),
		INFO(SimpleLog.LOG_LEVEL_INFO),
		WARN(SimpleLog.LOG_LEVEL_WARN),
		ERROR(SimpleLog.LOG_LEVEL_ERROR);

		private final int level;

		Level(int level) {
			this.level = level;
		}
	}
}
