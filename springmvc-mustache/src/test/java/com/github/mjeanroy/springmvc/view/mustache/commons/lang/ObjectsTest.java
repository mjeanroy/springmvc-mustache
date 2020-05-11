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

package com.github.mjeanroy.springmvc.view.mustache.commons.lang;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectsTest {

	@Test
	public void it_should_check_for_equality() {
		assertThat(Objects.equals(null, null)).isTrue();
		assertThat(Objects.equals(null, "test")).isFalse();
		assertThat(Objects.equals("test", null)).isFalse();
		assertThat(Objects.equals("test", "test")).isTrue();
		assertThat(Objects.equals("test1", "test2")).isFalse();
	}

	@Test
	public void it_should_compute_hash_code() {
		assertThat(Objects.hashCode(null)).isZero();
		assertThat(Objects.hashCode(null, null)).isNotZero();
		assertThat(Objects.hashCode("test")).isNotZero();
		assertThat(Objects.hashCode("test1", "test2")).isNotZero();
	}
}
