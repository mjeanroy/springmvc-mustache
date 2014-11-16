/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.springmvc.view.mustache.mustachejava;

import java.io.Writer;

import com.github.mustachejava.Iteration;
import com.github.mustachejava.reflect.ReflectionObjectHandler;

/**
 * Reflection Handler that treat number zero as a falsey value.
 * See: https://github.com/spullara/mustache.java/pull/111
 */
public class SpringMustacheReflectionObjectHandler extends ReflectionObjectHandler {

	@Override
	public Writer falsey(Iteration iteration, Writer writer, Object object, Object[] scopes) {
		return isZero(object) ? iteration.next(writer, object, scopes) : super.falsey(iteration, writer, object, scopes);
	}

	@Override
	public Writer iterate(Iteration iteration, Writer writer, Object object, Object[] scopes) {
		return isZero(object) ? writer : super.iterate(iteration, writer, object, scopes);
	}

	private boolean isZero(Object object) {
		return object instanceof Number && ((Number) object).intValue() == 0;
	}
}
