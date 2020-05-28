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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

/**
 * Implementation of {@link Logger} backed by SLF4J.
 */
class CommonsLoggingLogger implements Logger {

	/**
	 * Internal logger from commons-logging.
	 */
	private final Log log;

	CommonsLoggingLogger(Class<?> klass) {
		this.log = LogFactory.getLog(klass);
	}

	@Override
	public void trace(String message) {
		log.trace(message);
	}

	@Override
	public void trace(String message, Object arg) {
		if (isTraceEnabled()) {
			log.trace(interpolateMessage(message, singleton(arg)));
		}
	}

	@Override
	public void trace(String message, Object arg1, Object arg2) {
		if (isTraceEnabled()) {
			log.trace(interpolateMessage(message, asList(arg1, arg2)));
		}
	}

	@Override
	public void trace(String message, Throwable ex) {
		log.trace(message, ex);
	}

	@Override
	public void debug(String message) {
		log.debug(message);
	}

	@Override
	public void debug(String message, Object arg) {
		if (isDebugEnabled()) {
			log.debug(interpolateMessage(message, singleton(arg)));
		}
	}

	@Override
	public void debug(String message, Object arg1, Object arg2) {
		if (isDebugEnabled()) {
			log.debug(interpolateMessage(message, asList(arg1, arg2)));
		}
	}

	@Override
	public void debug(String message, Throwable ex) {
		log.debug(message, ex);
	}

	@Override
	public void info(String message) {
		log.info(message);
	}

	@Override
	public void info(String message, Object arg) {
		if (isInfoEnabled()) {
			log.info(interpolateMessage(message, singleton(arg)));
		}
	}

	@Override
	public void info(String message, Object arg1, Object arg2) {
		if (isInfoEnabled()) {
			log.info(interpolateMessage(message, asList(arg1, arg2)));
		}
	}

	@Override
	public void info(String message, Throwable ex) {
		log.info(message, ex);
	}

	@Override
	public void warn(String message) {
		log.warn(message);
	}

	@Override
	public void warn(String message, Object arg) {
		if (isWarnEnabled()) {
			log.warn(interpolateMessage(message, singleton(arg)));
		}
	}

	@Override
	public void warn(String message, Object arg1, Object arg2) {
		if (isWarnEnabled()) {
			log.warn(interpolateMessage(message, asList(arg1, arg2)));
		}
	}

	@Override
	public void warn(String message, Throwable ex) {
		log.warn(message, ex);
	}

	@Override
	public void error(String message) {
		log.error(message);
	}

	@Override
	public void error(String message, Object arg) {
		if (isErrorEnabled()) {
			log.error(interpolateMessage(message, singleton(arg)));
		}
	}

	@Override
	public void error(String message, Object arg1, Object arg2) {
		if (isErrorEnabled()) {
			log.error(interpolateMessage(message, asList(arg1, arg2)));
		}
	}

	@Override
	public void error(String message, Throwable ex) {
		log.error(message, ex);
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	private String interpolateMessage(String message, Collection<Object> args) {
		for (Object arg : args) {
			message = message.replaceFirst("\\{}", arg == null ? "null" : arg.toString());
		}

		return message;
	}
}
