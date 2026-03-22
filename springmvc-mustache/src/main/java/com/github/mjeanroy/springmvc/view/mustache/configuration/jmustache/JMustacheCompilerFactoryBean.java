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

package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.Collection;

import static java.util.Collections.emptyList;

/// Factory used to create instance of [com.samskivert.mustache.Mustache.Compiler].
/// Bean instance is thread safe.
public class JMustacheCompilerFactoryBean extends AbstractFactoryBean<Mustache.Compiler> {

	private static final Logger log = LoggerFactory.getLogger(JMustacheCompilerFactoryBean.class);

	/// A value to use when a variable resolves to null. If this value is null (which is the
	/// default `null` value), an exception will be thrown.
	///
	/// @see com.samskivert.mustache.Mustache.Compiler#nullValue
	private String nullValue;

	/// Use the given value for any variable that is missing, or otherwise resolves to `null`.
	///
	/// @see com.samskivert.mustache.Mustache.Compiler#defaultValue
	private String defaultValue;

	/// If this value is `true`, empty string will be treated as a `` value, as in JavaScript
	/// mustache implementation.
	///
	/// @see com.samskivert.mustache.Mustache.Compiler#emptyStringIsFalse
	private Boolean emptyStringIsFalse;

	/// If this value is `true`, zero will be treated as a `false` value, as in JavaScript
	/// mustache implementation.
	///
	/// @see com.samskivert.mustache.Mustache.Compiler#zeroIsFalse
	private Boolean zeroIsFalse;

	/// Does or does not escape HTML by default.
	///
	/// @see com.samskivert.mustache.Mustache.Compiler#escapeHTML
	private Boolean escapeHTML;

	/// Whether or not to throw an exception when a section resolves to a missing value. If
	/// `false`, the section is simply omitted (or included in the case of inverse sections).
	///
	/// @see com.samskivert.mustache.Mustache.Compiler#strictSections
	private Boolean strictSections;

	/// Whether or not standards mode is enabled.
	///
	/// @see com.samskivert.mustache.Mustache.Compiler#standardsMode
	private Boolean standardsMode;

	/// List of customizers that will be applied on [Mustache.Compiler] instance
	/// before creating object instance.
	private Collection<JMustacheCustomizer> customizers;

	/// Create factory with default settings.
	public JMustacheCompilerFactoryBean() {
		super();
		this.customizers = emptyList();
		this.nullValue = "";
		this.defaultValue = "";
		this.emptyStringIsFalse = true;
		this.zeroIsFalse = true;
		this.escapeHTML = true;
		this.strictSections = false;
		this.standardsMode = false;
	}

	@Override
	public Class<?> getObjectType() {
		return Mustache.Compiler.class;
	}

	@Override
	protected Mustache.Compiler createInstance() {
		log.debug("Create instance of {}", Mustache.Compiler.class);
		log.debug(" - nullValue = {}", nullValue);
		log.debug(" - defaultValue = {}", defaultValue);
		log.debug(" - emptyStringIsFalse = {}", emptyStringIsFalse);
		log.debug(" - zeroIsFalse = {}", zeroIsFalse);
		log.debug(" - escapeHTML = {}", escapeHTML);
		log.debug(" - strictSections = {}", strictSections);
		log.debug(" - standardsMode = {}", standardsMode);

		Mustache.Compiler compiler = Mustache.compiler();

		if (nullValue != null) {
			compiler = compiler.nullValue(nullValue);
		}

		if (defaultValue != null) {
			compiler = compiler.defaultValue(defaultValue);
		}

		if (emptyStringIsFalse != null) {
			compiler = compiler.emptyStringIsFalse(emptyStringIsFalse);
		}

		if (zeroIsFalse != null) {
			compiler = compiler.zeroIsFalse(zeroIsFalse);
		}

		if (escapeHTML != null) {
			compiler = compiler.escapeHTML(escapeHTML);
		}

		if (strictSections != null) {
			compiler = compiler.strictSections(strictSections);
		}

		if (standardsMode != null) {
			compiler = compiler.standardsMode(standardsMode);
		}

		if (customizers != null && !customizers.isEmpty()) {
			log.debug("Applying JMustache customizers");
			for (JMustacheCustomizer customizer : customizers) {
				log.debug(" - Applying JMustache customizer: {}", customizer);
				compiler = customizer.customize(compiler);
			}
		}

		return compiler;
	}

	/// Set [#nullValue]
	///
	/// @param nullValue [#nullValue]
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	/// Set [#defaultValue]
	///
	/// @param defaultValue [#defaultValue]
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/// Set [#emptyStringIsFalse]
	///
	/// @param emptyStringIsFalse [#emptyStringIsFalse]
	public void setEmptyStringIsFalse(boolean emptyStringIsFalse) {
		this.emptyStringIsFalse = emptyStringIsFalse;
	}

	/// Set [#zeroIsFalse]
	///
	/// @param zeroIsFalse [#zeroIsFalse]
	public void setZeroIsFalse(boolean zeroIsFalse) {
		this.zeroIsFalse = zeroIsFalse;
	}

	/// Set [#escapeHTML]
	///
	/// @param escapeHTML [#escapeHTML]
	public void setEscapeHTML(boolean escapeHTML) {
		this.escapeHTML = escapeHTML;
	}

	/// Set [#strictSections]
	///
	/// @param strictSections [#strictSections]
	public void setStrictSections(boolean strictSections) {
		this.strictSections = strictSections;
	}

	/// Set [#standardsMode]
	///
	/// @param standardsMode [#standardsMode]
	public void setStandardsMode(boolean standardsMode) {
		this.standardsMode = standardsMode;
	}

	/// Set [#customizers]
	///
	/// @param customizers [#customizers]
	public void setCustomizers(Collection<JMustacheCustomizer> customizers) {
		this.customizers = customizers;
	}
}
