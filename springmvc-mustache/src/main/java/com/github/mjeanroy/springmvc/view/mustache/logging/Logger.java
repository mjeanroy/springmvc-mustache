/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 Mickael Jeanroy
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

/// Logging abstraction.
public interface Logger {

	/// Log message with level `TRACE`.
	///
	/// @param message Message to log.
	void trace(String message);

	/// Log message with level `TRACE` and with given parameter.
	///
	/// @param message Message to log.
	/// @param arg Log message parameter.
	void trace(String message, Object arg);

	/// Log message with level `TRACE` and with given parameters.
	///
	/// @param message Message to log.
	/// @param arg1 First log message parameter.
	/// @param arg2 Second log message parameter.
	void trace(String message, Object arg1, Object arg2);

	/// Log message with level `TRACE` and log given exception stacktrace.
	///
	/// @param message Message to log.
	/// @param ex The exception.
	void trace(String message, Throwable ex);

	/// Log message with level `DEBUG`.
	///
	/// @param message Message to log.
	void debug(String message);

	/// Log message with level `DEBUG` and with given parameter.
	///
	/// @param message Message to log.
	/// @param arg Log message parameter.
	void debug(String message, Object arg);

	/// Log message with level `DEBUG` and with given parameters.
	///
	/// @param message Message to log.
	/// @param arg1 First log message parameter.
	/// @param arg2 Second log message parameter.
	void debug(String message, Object arg1, Object arg2);

	/// Log message with level `DEBUG` and log given exception stacktrace.
	///
	/// @param message Message to log.
	/// @param ex The exception.
	void debug(String message, Throwable ex);

	/// Log message with level `INFO`.
	///
	/// @param message Message to log.
	void info(String message);

	/// Log message with level `INFO` and with given parameter.
	///
	/// @param message Message to log.
	/// @param arg Log message parameter.
	void info(String message, Object arg);

	/// Log message with level `INFO` and with given parameters.
	///
	/// @param message Message to log.
	/// @param arg1 First log message parameter.
	/// @param arg2 Second log message parameter.
	void info(String message, Object arg1, Object arg2);

	/// Log message with level `INFO` and log given exception stacktrace.
	///
	/// @param message Message to log.
	/// @param ex The exception.
	void info(String message, Throwable ex);

	/// Log message with level `WARN`.
	///
	/// @param message Message to log.
	void warn(String message);

	/// Log message with level `WARN` and with given parameter.
	///
	/// @param message Message to log.
	/// @param arg Log message parameter.
	void warn(String message, Object arg);

	/// Log message with level `WARN` and with given parameters.
	///
	/// @param message Message to log.
	/// @param arg1 First log message parameter.
	/// @param arg2 Second log message parameter.
	void warn(String message, Object arg1, Object arg2);

	/// Log message with level `WARN` and log given exception stacktrace.
	///
	/// @param message Message to log.
	/// @param ex The exception.
	void warn(String message, Throwable ex);

	/// Log message with level `ERROR`.
	///
	/// @param message Message to log.
	void error(String message);

	/// Log message with level `ERROR` and with given parameter.
	///
	/// @param message Message to log.
	/// @param arg Log message parameter.
	void error(String message, Object arg);

	/// Log message with level `ERROR` and with given parameters.
	///
	/// @param message Message to log.
	/// @param arg1 First log message parameter.
	/// @param arg2 Second log message parameter.
	void error(String message, Object arg1, Object arg2);

	/// Log message with level `ERROR` and log given exception stacktrace.
	///
	/// @param message Message to log.
	/// @param ex The exception.
	void error(String message, Throwable ex);

	/// Check if `TRACE` is enabled.
	///
	/// @return `true` if `TRACE` level is enabled, `false` otherwise.
	boolean isTraceEnabled();

	/// Check if `DEBUG` is enabled.
	///
	/// @return `true` if `DEBUG` level is enabled, `false` otherwise.
	boolean isDebugEnabled();

	/// Check if `INFO` is enabled.
	///
	/// @return `true` if `INFO` level is enabled, `false` otherwise.
	boolean isInfoEnabled();

	/// Check if `WARN` is enabled.
	///
	/// @return `true` if `WARN` level is enabled, `false` otherwise.
	boolean isWarnEnabled();

	/// Check if `ERROR` is enabled.
	///
	/// @return `true` if `ERROR` level is enabled, `false` otherwise.
	boolean isErrorEnabled();
}
