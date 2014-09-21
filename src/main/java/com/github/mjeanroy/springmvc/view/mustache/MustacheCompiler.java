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

package com.github.mjeanroy.springmvc.view.mustache;

import java.util.Map;

/**
 * Mustache compiler that can be used to compile mustache
 * templates with optional partials.
 */
public interface MustacheCompiler {

	/**
	 * Compile template.
	 *
	 * @param name Template name.
	 * @return Compiled template.
	 */
	MustacheTemplate compile(String name);

	/**
	 * Set prefix to prepend to template names before it is compiled.
	 *
	 * @param prefix New prefix value.
	 */
	void setPrefix(String prefix);

	/**
	 * Set suffix to append to template names before it is compiled.
	 *
	 * @param suffix New suffix value.
	 */
	void setSuffix(String suffix);

	/**
	 * Get prefix prepended to template names.
	 *
	 * @return Prefix.
	 */
	String getPrefix();

	/**
	 * Get suffix appended to template names.
	 *
	 * @return Suffix.
	 */
	String getSuffix();

	/**
	 * Add temporary partials aliases.
	 * These partials can be removed later with {@link #removeTemporaryPartialAliases()}
	 * method.
	 * Implementation should be thread safe.
	 *
	 * @param partialAliases Partials aliases to add.
	 */
	void addTemporaryPartialAliases(Map<String, String> partialAliases);

	/**
	 * Remove temporary partial aliases previously added
	 * with {@link #addTemporaryPartialAliases(java.util.Map)} method.
	 * Implementation should be thread safe.
	 */
	void removeTemporaryPartialAliases();
}
