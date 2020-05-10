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

import com.github.mjeanroy.springmvc.view.mustache.commons.reflection.Classes;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Factory for {@link Logger}.
 */
public final class LoggerFactory {

	/**
	 * The custom logger provider provided using the Service Provider Interface.
	 */
	private static final LoggerProvider loggerProvider;

	static {
		// First, discover using the ServiceProvider API.
		ServiceLoader<LoggerProvider> loggerProviders = ServiceLoader.load(LoggerProvider.class);
		Iterator<LoggerProvider> it = loggerProviders.iterator();
		loggerProvider = it.hasNext() ? it.next() : null;
	}

	// Ensure non instantiation.
	private LoggerFactory() {
	}

	/**
	 * Create logger from given class name as logger name.
	 *
	 * @param klass Class name.
	 * @return Logger.
	 */
	public static Logger getLogger(Class<?> klass) {
		// First, discover using the ServiceProvider API.
		if (loggerProvider != null) {
			return loggerProvider.getLogger(klass);
		}

		if (Classes.isPresent("org.slf4j.Logger")) {
			return new Slf4jLogger(klass);
		}

		if (Classes.isPresent("org.apache.logging.log4j.Logger")) {
			return new Log4j2Logger(klass);
		}

		if (Classes.isPresent("org.apache.commons.logging.Log")) {
			return new CommonsLoggingLogger(klass);
		}

		return NoopLogger.getInstance();
	}
}
