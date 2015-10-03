package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Factory used to create instance of {@link com.samskivert.mustache.Mustache.Compiler}.
 * Bean instance is thread safe.
 */
public class JMustacheCompilerFactoryBean extends AbstractFactoryBean<Mustache.Compiler> implements FactoryBean<Mustache.Compiler> {

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
