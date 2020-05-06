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

package com.github.mjeanroy.springmvc.view.mustache.tests;

import org.junit.rules.ExternalResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Catch a printable stream and store in a buffer.
 */
abstract class AbstracrCaptureOutputRule extends ExternalResource {

	/**
	 * Original out stream.
	 * Will be initialized before each tests.
	 * Will be restored after each tests.
	 */
	private PrintStream originalOut;

	/**
	 * Custom out stream.
	 * Will be initialized before each tests.
	 * Will be flushed after each tests.
	 */
	private ByteArrayOutputStream out;

	@Override
	public final void before() {
		originalOut = System.out;
		out = new ByteArrayOutputStream();

		overrideOutput(
				new PrintStream(this.out)
		);
	}

	@Override
	public final void after() {
		try {
			out.reset();
			out.flush();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			// No worries
		}

		// Restore original out stream
		overrideOutput(originalOut);
	}

	abstract void overrideOutput(PrintStream ps);

	public String getOut() {
		return out == null ? null : out.toString();
	}
}
