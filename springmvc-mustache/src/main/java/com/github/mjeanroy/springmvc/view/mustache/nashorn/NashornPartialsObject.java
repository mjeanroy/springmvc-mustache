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

package com.github.mjeanroy.springmvc.view.mustache.nashorn;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.Objects;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import jdk.nashorn.api.scripting.AbstractJSObject;

import java.io.Reader;

import static com.github.mjeanroy.springmvc.view.mustache.commons.io.Ios.read;
import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.notNull;

/**
 * Implementation of dynamic partial object.
 *
 * Instance of this object will be used by nashorn engine to get
 * template values.
 *
 * This is more like a dynamic map object: each key is evaluated to return
 * associate template.
 *
 * Note that this object is a read-only object (you cannot add entry).
 *
 * This object is thread safe.
 *
 * @deprecated Nashorn will be removed after jdk 11, so nashorn engine ill be removed in the next major version.
 */
@Deprecated
final class NashornPartialsObject extends AbstractJSObject {

	/**
	 * Internal template loader implementation.
	 */
	private final MustacheTemplateLoader templateLoader;

	/**
	 * Create nashorn partial object.
	 *
	 * @param templateLoader Template loader.
	 */
	NashornPartialsObject(MustacheTemplateLoader templateLoader) {
		this.templateLoader = notNull(templateLoader, "Template Loader must not be null");
	}

	@Override
	public boolean hasMember(String name) {
		// No worries, il will fail later...
		return true;
	}

	@Override
	public Object getMember(String name) {
		Reader reader = templateLoader.getTemplate(name);
		return read(reader);
	}

	@Override
	public void removeMember(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMember(String name, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSlot(int index, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("templateLoader", templateLoader)
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof NashornPartialsObject) {
			NashornPartialsObject p = (NashornPartialsObject) o;
			return Objects.equals(templateLoader, p.templateLoader);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(templateLoader);
	}
}
