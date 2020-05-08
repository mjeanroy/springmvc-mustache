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

/**
 * Implementation of {@link Logger} that does nothing.
 */
class NoopLogger implements Logger {

	private static final NoopLogger INSTANCE = new NoopLogger();

	static NoopLogger getInstance() {
		return INSTANCE;
	}

	private NoopLogger() {
	}

	@Override
	public void trace(String message) {
	}

	@Override
	public void trace(String message, Object arg) {
	}

	@Override
	public void trace(String message, Object arg1, Object arg2) {
	}

	@Override
	public void trace(String message, Throwable ex) {
	}

	@Override
	public void debug(String message) {
	}

	@Override
	public void debug(String message, Object arg) {
	}

	@Override
	public void debug(String message, Object arg1, Object arg2) {
	}

	@Override
	public void debug(String message, Throwable ex) {
	}

	@Override
	public void info(String message) {
	}

	@Override
	public void info(String message, Object arg) {
	}

	@Override
	public void info(String message, Object arg1, Object arg2) {
	}

	@Override
	public void info(String message, Throwable ex) {
	}

	@Override
	public void warn(String message) {
	}

	@Override
	public void warn(String message, Object arg) {
	}

	@Override
	public void warn(String message, Object arg1, Object arg2) {
	}

	@Override
	public void warn(String message, Throwable ex) {
	}

	@Override
	public void error(String message) {
	}

	@Override
	public void error(String message, Object arg) {
	}

	@Override
	public void error(String message, Object arg1, Object arg2) {
	}

	@Override
	public void error(String message, Throwable ex) {
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public boolean isWarnEnabled() {
		return false;
	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}
}
