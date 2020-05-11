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

package com.github.mjeanroy.springmvc.view.mustache.mustachejava;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.Objects;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import com.github.mjeanroy.springmvc.view.mustache.core.AbstractMustacheTemplate;
import com.github.mustachejava.Mustache;

import java.io.Writer;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.notNull;

/**
 * Implementation of {@link com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate} using
 * mustache.java as real template implementation.
 */
final class MustacheJavaTemplate extends AbstractMustacheTemplate implements MustacheTemplate {

	/**
	 * Mustache.java template.
	 * This template will be rendered using mustache.java api.
	 */
	private final Mustache mustache;

	/**
	 * Build new template.
	 *
	 * @param mustache Mustache template.
	 */
	public MustacheJavaTemplate(Mustache mustache) {
		this.mustache = notNull(mustache, "Mustache template must not be null");
	}

	@Override
	protected void doExecute(Map<String, Object> model, Writer writer) {
		mustache.execute(writer, model);
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("mustache", mustache)
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof MustacheJavaTemplate) {
			MustacheJavaTemplate t = (MustacheJavaTemplate) o;
			return Objects.equals(mustache, t.mustache);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(mustache);
	}
}
