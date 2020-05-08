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

import org.apache.logging.log4j.LogManager;

/**
 * Implementation of {@link Logger} backed by LOG4J2.
 */
class Log4j2Logger implements Logger {

	/**
	 * Internal logger.
	 */
	private final org.apache.logging.log4j.Logger log;

	Log4j2Logger(Class<?> klass) {
		this.log = LogManager.getLogger(klass);
	}

	@Override
	public void trace(String message) {
		log.trace(message);
	}

	@Override
	public void trace(String message, Object arg) {
		log.trace(message, arg);
	}

	@Override
	public void trace(String message, Object arg1, Object arg2) {
		log.trace(message, arg1, arg2);
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
		log.debug(message, arg);
	}

	@Override
	public void debug(String message, Object arg1, Object arg2) {
		log.debug(message, arg1, arg2);
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
		log.info(message, arg);
	}

	@Override
	public void info(String message, Object arg1, Object arg2) {
		log.info(message, arg1, arg2);
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
		log.warn(message, arg);
	}

	@Override
	public void warn(String message, Object arg1, Object arg2) {
		log.warn(message, arg1, arg2);
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
		log.error(message, arg);
	}

	@Override
	public void error(String message, Object arg1, Object arg2) {
		log.error(message, arg1, arg2);
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
}
