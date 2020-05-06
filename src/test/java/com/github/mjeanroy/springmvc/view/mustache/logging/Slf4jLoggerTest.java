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

import ch.qos.logback.classic.Level;
import com.github.mjeanroy.springmvc.view.mustache.tests.SystemOutRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;

public class Slf4jLoggerTest {

	@Rule
	public SystemOutRule systemOutRule = new SystemOutRule();

	private Logger log;

	@Before
	public void setUp() {
		log = createLogger();
	}

	@Test
	public void it_should_log_trace_message_without_argument() {
		log.trace("Message with placeholder");
		verifyOutput("TRACE", "Message with placeholder");
	}

	@Test
	public void it_should_log_trace_message_with_one_argument() {
		log.trace("Message with placeholder: {}", "arg1");
		verifyOutput("TRACE", "Message with placeholder: arg1");
	}

	@Test
	public void it_should_log_trace_message_with_two_arguments() {
		log.trace("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput("TRACE", "Message with placeholder: arg1 arg2");
	}

	@Test
	public void it_should_log_throwable_using_trace_level() {
		Exception ex = new RuntimeException("A runtime exception");
		String message = "error message";
		log.trace(message, ex);

		verifyOutput("TRACE", ex.getMessage());
	}

	@Test
	public void it_should_check_if_trace_level_is_enabled() {
		updateLevel("TRACE");
		assertThat(log.isTraceEnabled()).isTrue();

		updateLevel("DEBUG");
		assertThat(log.isTraceEnabled()).isFalse();

		updateLevel("INFO");
		assertThat(log.isTraceEnabled()).isFalse();

		updateLevel("WARN");
		assertThat(log.isTraceEnabled()).isFalse();

		updateLevel("ERROR");
		assertThat(log.isTraceEnabled()).isFalse();
	}

	@Test
	public void it_should_log_debug_message_with_one_argument() {
		log.debug("Message with placeholder: {}", "arg1");
		verifyOutput("DEBUG", "Message with placeholder: arg1");
	}

	@Test
	public void it_should_log_debug_message_without_argument() {
		log.debug("Message with placeholder");
		verifyOutput("DEBUG", "Message with placeholder");
	}

	@Test
	public void it_should_log_debug_message_with_two_arguments() {
		log.debug("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput("DEBUG", "Message with placeholder: arg1 arg2");
	}

	@Test
	public void it_should_log_throwable_using_debug_level() {
		Exception ex = new RuntimeException("A runtime exception");
		String message = "error message";
		log.debug(message, ex);

		verifyOutput("DEBUG", ex.getMessage());
	}

	@Test
	public void it_should_check_if_debug_level_is_enabled() {
		updateLevel("TRACE");
		assertThat(log.isDebugEnabled()).isTrue();

		updateLevel("DEBUG");
		assertThat(log.isDebugEnabled()).isTrue();

		updateLevel("INFO");
		assertThat(log.isDebugEnabled()).isFalse();

		updateLevel("WARN");
		assertThat(log.isDebugEnabled()).isFalse();

		updateLevel("ERROR");
		assertThat(log.isDebugEnabled()).isFalse();
	}

	@Test
	public void it_should_log_info_message_with_one_argument() {
		log.info("Message with placeholder: {}", "arg1");
		verifyOutput("INFO", "Message with placeholder: arg1");
	}

	@Test
	public void it_should_log_info_message_without_argument() {
		log.info("Message with placeholder");
		verifyOutput("INFO", "Message with placeholder");
	}

	@Test
	public void it_should_log_info_message_with_two_arguments() {
		log.info("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput("INFO", "Message with placeholder: arg1 arg2");
	}

	@Test
	public void it_should_log_throwable_using_info_level() {
		Exception ex = new RuntimeException("A runtime exception");
		String message = "error message";
		log.info(message, ex);

		verifyOutput("INFO", ex.getMessage());
	}

	@Test
	public void it_should_check_if_info_level_is_enabled() {
		updateLevel("TRACE");
		assertThat(log.isInfoEnabled()).isTrue();

		updateLevel("DEBUG");
		assertThat(log.isInfoEnabled()).isTrue();

		updateLevel("INFO");
		assertThat(log.isInfoEnabled()).isTrue();

		updateLevel("WARN");
		assertThat(log.isInfoEnabled()).isFalse();

		updateLevel("ERROR");
		assertThat(log.isInfoEnabled()).isFalse();
	}

	@Test
	public void it_should_log_warn_message_without_argument() {
		log.warn("Message with placeholder");
		verifyOutput("WARN", "Message with placeholder");
	}

	@Test
	public void it_should_log_warn_message_with_one_argument() {
		log.warn("Message with placeholder: {}", "arg1");
		verifyOutput("WARN", "Message with placeholder: arg1");
	}

	@Test
	public void it_should_log_warn_message_with_two_arguments() {
		log.warn("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput("WARN", "Message with placeholder: arg1 arg2");
	}

	@Test
	public void it_should_log_throwable_using_warn_level() {
		Exception ex = new RuntimeException("A runtime exception");
		String message = "error message";
		log.warn(message, ex);

		verifyOutput("WARN", ex.getMessage());
	}

	@Test
	public void it_should_check_if_warn_level_is_enabled() {
		updateLevel("TRACE");
		assertThat(log.isWarnEnabled()).isTrue();

		updateLevel("DEBUG");
		assertThat(log.isWarnEnabled()).isTrue();

		updateLevel("INFO");
		assertThat(log.isWarnEnabled()).isTrue();

		updateLevel("WARN");
		assertThat(log.isWarnEnabled()).isTrue();

		updateLevel("ERROR");
		assertThat(log.isInfoEnabled()).isFalse();
	}

	@Test
	public void it_should_log_error_message_without_argument() {
		log.error("Message with placeholder");
		verifyOutput("ERROR", "Message with placeholder");
	}

	@Test
	public void it_should_log_error_message_with_one_argument() {
		log.error("Message with placeholder: {}", "arg1");
		verifyOutput("ERROR", "Message with placeholder: arg1");
	}

	@Test
	public void it_should_log_error_message_with_two_arguments() {
		log.error("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput("ERROR", "Message with placeholder: arg1 arg2");
	}

	@Test
	public void it_should_log_throwable_using_error_level() {
		Exception ex = new RuntimeException("A runtime exception");
		String message = "error message";
		log.error(message, ex);

		verifyOutput("ERROR", ex.getMessage());
	}

	@Test
	public void it_should_check_if_error_level_is_enabled() {
		updateLevel("TRACE");
		assertThat(log.isErrorEnabled()).isTrue();

		updateLevel("DEBUG");
		assertThat(log.isErrorEnabled()).isTrue();

		updateLevel("INFO");
		assertThat(log.isErrorEnabled()).isTrue();

		updateLevel("WARN");
		assertThat(log.isErrorEnabled()).isTrue();

		updateLevel("ERROR");
		assertThat(log.isErrorEnabled()).isTrue();
	}

	private void verifyOutput(String logLevel, String message) {
		String out = systemOutRule.getOut();
		assertThat(out).contains(logLevel);
		assertThat(out).contains(message);
	}

	private Logger createLogger() {
		Slf4jLogger log = new Slf4jLogger(getClass());

		ch.qos.logback.classic.Logger logback = readField(log, "log");
		logback.setLevel(Level.TRACE);
		logback.setAdditive(true);

		return log;
	}

	private void updateLevel(String level) {
		ch.qos.logback.classic.Logger logback = readField(log, "log");
		logback.setLevel(Level.valueOf(level));
		logback.setAdditive(true);
	}
}
