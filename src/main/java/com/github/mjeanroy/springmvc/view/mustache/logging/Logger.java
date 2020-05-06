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
 * Logging abstraction.
 */
public interface Logger {

	/**
	 * Log message with level {@code TRACE}.
	 *
	 * @param message Message to log.
	 */
	void trace(String message);

	/**
	 * Log message with level {@code TRACE} and with given parameter.
	 *
	 * @param message Message to log.
	 * @param arg Log message parameter.
	 */
	void trace(String message, Object arg);

	/**
	 * Log message with level {@code TRACE} and with given parameters.
	 *
	 * @param message Message to log.
	 * @param arg1 First log message parameter.
	 * @param arg2 Second log message parameter.
	 */
	void trace(String message, Object arg1, Object arg2);

	/**
	 * Log message with level {@code TRACE} and log given exception stacktrace.
	 *
	 * @param message Message to log.
	 * @param ex The exception.
	 */
	void trace(String message, Throwable ex);

	/**
	 * Log message with level {@code DEBUG}.
	 *
	 * @param message Message to log.
	 */
	void debug(String message);

	/**
	 * Log message with level {@code DEBUG} and with given parameter.
	 *
	 * @param message Message to log.
	 * @param arg Log message parameter.
	 */
	void debug(String message, Object arg);

	/**
	 * Log message with level {@code DEBUG} and with given parameters.
	 *
	 * @param message Message to log.
	 * @param arg1 First log message parameter.
	 * @param arg2 Second log message parameter.
	 */
	void debug(String message, Object arg1, Object arg2);

	/**
	 * Log message with level {@code DEBUG} and log given exception stacktrace.
	 *
	 * @param message Message to log.
	 * @param ex The exception.
	 */
	void debug(String message, Throwable ex);

	/**
	 * Log message with level {@code INFO}.
	 *
	 * @param message Message to log.
	 */
	void info(String message);

	/**
	 * Log message with level {@code INFO} and with given parameter.
	 *
	 * @param message Message to log.
	 * @param arg Log message parameter.
	 */
	void info(String message, Object arg);

	/**
	 * Log message with level {@code INFO} and with given parameters.
	 *
	 * @param message Message to log.
	 * @param arg1 First log message parameter.
	 * @param arg2 Second log message parameter.
	 */
	void info(String message, Object arg1, Object arg2);

	/**
	 * Log message with level {@code INFO} and log given exception stacktrace.
	 *
	 * @param message Message to log.
	 * @param ex The exception.
	 */
	void info(String message, Throwable ex);

	/**
	 * Log message with level {@code WARN}.
	 *
	 * @param message Message to log.
	 */
	void warn(String message);

	/**
	 * Log message with level {@code WARN} and with given parameter.
	 *
	 * @param message Message to log.
	 * @param arg Log message parameter.
	 */
	void warn(String message, Object arg);

	/**
	 * Log message with level {@code WARN} and with given parameters.
	 *
	 * @param message Message to log.
	 * @param arg1 First log message parameter.
	 * @param arg2 Second log message parameter.
	 */
	void warn(String message, Object arg1, Object arg2);

	/**
	 * Log message with level {@code WARN} and log given exception stacktrace.
	 *
	 * @param message Message to log.
	 * @param ex The exception.
	 */
	void warn(String message, Throwable ex);

	/**
	 * Log message with level {@code ERROR}.
	 *
	 * @param message Message to log.
	 */
	void error(String message);

	/**
	 * Log message with level {@code ERROR} and with given parameter.
	 *
	 * @param message Message to log.
	 * @param arg Log message parameter.
	 */
	void error(String message, Object arg);

	/**
	 * Log message with level {@code ERROR} and with given parameters.
	 *
	 * @param message Message to log.
	 * @param arg1 First log message parameter.
	 * @param arg2 Second log message parameter.
	 */
	void error(String message, Object arg1, Object arg2);

	/**
	 * Log message with level {@code ERROR} and log given exception stacktrace.
	 *
	 * @param message Message to log.
	 * @param ex The exception.
	 */
	void error(String message, Throwable ex);

	/**
	 * Check if {@code TRACE} is enabled.
	 *
	 * @return {@code true} if {@code TRACE} level is enabled, {@code false} otherwise.
	 */
	boolean isTraceEnabled();

	/**
	 * Check if {@code DEBUG} is enabled.
	 *
	 * @return {@code true} if {@code DEBUG} level is enabled, {@code false} otherwise.
	 */
	boolean isDebugEnabled();

	/**
	 * Check if {@code INFO} is enabled.
	 *
	 * @return {@code true} if {@code INFO} level is enabled, {@code false} otherwise.
	 */
	boolean isInfoEnabled();

	/**
	 * Check if {@code WARN} is enabled.
	 *
	 * @return {@code true} if {@code WARN} level is enabled, {@code false} otherwise.
	 */
	boolean isWarnEnabled();

	/**
	 * Check if {@code ERROR} is enabled.
	 *
	 * @return {@code true} if {@code ERROR} level is enabled, {@code false} otherwise.
	 */
	boolean isErrorEnabled();
}
