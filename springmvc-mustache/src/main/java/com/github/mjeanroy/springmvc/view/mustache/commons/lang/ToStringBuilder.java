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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility to write {@code toString} methods easily.
 */
public class ToStringBuilder {

	private static final char IDENTITY_SEPARATOR = '@';
	private static final char OBJECT_PREFIX = '{';
	private static final char OBJECT_SUFFIX = '}';
	private static final char FIELD_VALUE_SEPARATOR = '=';
	private static final String FIELD_SEPARATOR = ", ";

	/**
	 * Create new builder.
	 *
	 * @param instance The instance to serialize as a string.
	 * @return The builder.
	 */
	public static ToStringBuilder builder(Object instance) {
		return new ToStringBuilder(instance);
	}

	/**
	 * The name of the class.
	 */
	private final String name;

	/**
	 * The identity instance.
	 */
	private final String identity;

	/**
	 * The list of fields to output.
	 */
	private final Map<String, String> fields;

	/**
	 * Size of the string output being builded.
	 *
	 * This is used to instantiate {@link StringBuilder} with the correct initial capacity.
	 */
	private int size;

	private ToStringBuilder(Object object) {
		this.name = object.getClass().getName();
		this.identity = Integer.toHexString(System.identityHashCode(object));
		this.fields = new LinkedHashMap<String, String>();
		this.size = name.length() + identity.length() + 1;
	}

	/**
	 * Append field to the final output.
	 *
	 * @param name  Field name.
	 * @param value Field value.
	 * @return The builder.
	 */
	public ToStringBuilder append(String name, String value) {
		return doAppend(name, formatValue(value));
	}

	/**
	 * Append field to the final output.
	 *
	 * @param name  Field name.
	 * @param value Field value.
	 * @return The builder.
	 */
	public ToStringBuilder append(String name, boolean value) {
		return doAppend(name, Boolean.toString(value));
	}

	/**
	 * Append field to the final output.
	 *
	 * @param name  Field name.
	 * @param value Field value.
	 * @return The builder.
	 */
	public ToStringBuilder append(String name, Object value) {
		return doAppend(name, value == null ? "null" : value.toString());
	}

	/**
	 * Append field to the final output and update internal state.
	 *
	 * @param name           Field name.
	 * @param formattedValue The (already formatted) field value.
	 * @return The builder.
	 */
	private ToStringBuilder doAppend(String name, String formattedValue) {
		this.fields.put(name, formattedValue);
		this.size += name.length();
		this.size += formattedValue.length();
		return this;
	}

	/**
	 * Build the final string output.
	 *
	 * @return The final output.
	 */
	public String build() {
		int nbFields = fields.size();
		int fullSize = this.size
				+ 1                                          // OBJECT_PREFIX
				+ 1                                          // OBJECT_SUFFIX
				+ nbFields                                   // FIELD_VALUE_SEPARATOR
				+ FIELD_SEPARATOR.length() * (nbFields - 1); // FIELD_SEPARATOR

		StringBuilder sb = new StringBuilder(fullSize);
		sb.append(name);
		sb.append(IDENTITY_SEPARATOR);
		sb.append(identity);
		sb.append(OBJECT_PREFIX);

		boolean first = true;
		for (Map.Entry<String, String> field : fields.entrySet()) {
			if (!first) {
				sb.append(FIELD_SEPARATOR);
			}

			sb.append(field.getKey()).append(FIELD_VALUE_SEPARATOR).append(field.getValue());
			first = false;
		}

		sb.append(OBJECT_SUFFIX);

		return sb.toString();
	}

	private static String formatValue(String value) {
		return value == null ? "null" : "\"" + value + "\"";
	}
}
