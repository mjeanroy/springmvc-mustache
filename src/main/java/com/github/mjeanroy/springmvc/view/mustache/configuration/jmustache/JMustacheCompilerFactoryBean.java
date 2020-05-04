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

package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.samskivert.mustache.Mustache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Factory used to create instance of {@link com.samskivert.mustache.Mustache.Compiler}.
 * Bean instance is thread safe.
 */
public class JMustacheCompilerFactoryBean extends AbstractFactoryBean<Mustache.Compiler> implements FactoryBean<Mustache.Compiler> {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(JMustacheCompilerFactoryBean.class);

	/**
	 * @see {@link com.samskivert.mustache.Mustache.Compiler#nullValue}
	 */
	private String nullValue;

	/**
	 * @see {@link com.samskivert.mustache.Mustache.Compiler#defaultValue}
	 */
	private String defaultValue;

	/**
	 * @see {@link com.samskivert.mustache.Mustache.Compiler#emptyStringIsFalse}
	 */
	private boolean emptyStringIsFalse;

	/**
	 * @see {@link com.samskivert.mustache.Mustache.Compiler#zeroIsFalse}
	 */
	private boolean zeroIsFalse;

	/**
	 * @see {@link com.samskivert.mustache.Mustache.Compiler#escapeHTML}
	 */
	private boolean escapeHTML;

	/**
	 * @see {@link com.samskivert.mustache.Mustache.Compiler#strictSections}
	 */
	private boolean strictSections;

	/**
	 * @see {@link com.samskivert.mustache.Mustache.Compiler#standardsMode}
	 */
	private boolean standardsMode;

	/**
	 * Create factory with default settings.
	 */
	public JMustacheCompilerFactoryBean() {
		super();
		nullValue = "";
		defaultValue = "";
		emptyStringIsFalse = true;
		zeroIsFalse = true;
		escapeHTML = true;
		strictSections = false;
		standardsMode = false;
	}

	@Override
	public Class<?> getObjectType() {
		return Mustache.Compiler.class;
	}

	@Override
	protected Mustache.Compiler createInstance() throws Exception {
		log.debug("Create instance of {}", Mustache.Compiler.class);
		return Mustache.compiler()
				.nullValue(nullValue)
				.defaultValue(defaultValue)
				.emptyStringIsFalse(emptyStringIsFalse)
				.zeroIsFalse(zeroIsFalse)
				.escapeHTML(escapeHTML)
				.strictSections(strictSections)
				.standardsMode(standardsMode);
	}

	/**
	 * Set {@link #nullValue}
	 *
	 * @param nullValue {@link #nullValue}
	 */
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	/**
	 * Set {@link #defaultValue}
	 *
	 * @param defaultValue {@link #defaultValue}
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Set {@link #emptyStringIsFalse}
	 *
	 * @param emptyStringIsFalse {@link #emptyStringIsFalse}
	 */
	public void setEmptyStringIsFalse(boolean emptyStringIsFalse) {
		this.emptyStringIsFalse = emptyStringIsFalse;
	}

	/**
	 * Set {@link #zeroIsFalse}
	 *
	 * @param zeroIsFalse {@link #zeroIsFalse}
	 */
	public void setZeroIsFalse(boolean zeroIsFalse) {
		this.zeroIsFalse = zeroIsFalse;
	}

	/**
	 * Set {@link #escapeHTML}
	 *
	 * @param escapeHTML {@link #escapeHTML}
	 */
	public void setEscapeHTML(boolean escapeHTML) {
		this.escapeHTML = escapeHTML;
	}

	/**
	 * Set {@link #strictSections}
	 *
	 * @param strictSections {@link #strictSections}
	 */
	public void setStrictSections(boolean strictSections) {
		this.strictSections = strictSections;
	}

	/**
	 * Set {@link #standardsMode}
	 *
	 * @param standardsMode {@link #standardsMode}
	 */
	public void setStandardsMode(boolean standardsMode) {
		this.standardsMode = standardsMode;
	}
}
