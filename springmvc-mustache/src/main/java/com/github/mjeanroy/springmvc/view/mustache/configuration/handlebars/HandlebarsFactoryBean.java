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

package com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.Collection;

import static java.util.Collections.emptyList;

/**
 * Factory used to create instance of {@link com.github.jknack.handlebars.Handlebars}.
 */
public class HandlebarsFactoryBean extends AbstractFactoryBean<Handlebars> {

	private static final Logger log = LoggerFactory.getLogger(HandlebarsFactoryBean.class);

	/**
	 * Handlebars start delimiter.
	 *
	 * @see Handlebars#setStartDelimiter(String)
	 */
	private String startDelimiter;

	/**
	 * Handlebars end delimiter.
	 *
	 * @see Handlebars#setEndDelimiter(String)
	 */
	private String endDelimiter;

	/**
	 * If {@code true}, missing helper parameters will be resolve to their names.
	 *
	 * @see Handlebars#stringParams()
	 */
	private Boolean stringParams;

	/**
	 * If {@code true}, templates will be deleted once applied.
	 *
	 * @see Handlebars#deletePartialAfterMerge()
	 */
	private Boolean deletePartialAfterMerge;

	/**
	 * If {@code true}, templates will be able to call him self directly or indirectly
	 *
	 * @see Handlebars#infiniteLoops()
	 */
	private Boolean infiniteLoops;

	/**
	 * Set to {@code true}, if we want to extend lookup to parent scope, like Mustache Spec.
	 * Or {@code false}, if lookup is restricted to current scope, like handlebars.js.
	 *
	 * @see Handlebars#parentScopeResolution()
	 */
	private Boolean parentScopeResolution;

	/**
	 * If {@code true}, unnecessary spaces and new lines will be removed from output.
	 *
	 * @see Handlebars#prettyPrint()
	 */
	private Boolean prettyPrint;

	/**
	 * List of customizers that will be applied on {@link Handlebars} instance
	 * before creating object instance.
	 */
	private Collection<HandlebarsCustomizer> customizers;

	/**
	 * Create factory with default settings.
	 */
	public HandlebarsFactoryBean() {
		super();
		this.customizers = emptyList();
	}

	@Override
	public Class<?> getObjectType() {
		return Handlebars.class;
	}

	@Override
	protected Handlebars createInstance() {
		log.debug("Create instance of {}", Handlebars.class);
		log.debug(" - stringParams = {}", stringParams);
		log.debug(" - deletePartialAfterMerge = {}", deletePartialAfterMerge);
		log.debug(" - startDelimiter = {}", startDelimiter);
		log.debug(" - endDelimiter = {}", endDelimiter);
		log.debug(" - infiniteLoops = {}", infiniteLoops);
		log.debug(" - parentScopeResolution = {}", parentScopeResolution);
		log.debug(" - prettyPrint = {}", prettyPrint);

		Handlebars handlebars = new Handlebars();

		if (startDelimiter != null) {
			handlebars.setStartDelimiter(startDelimiter);
		}

		if (endDelimiter != null) {
			handlebars.setEndDelimiter(endDelimiter);
		}

		if (stringParams != null) {
			handlebars.setStringParams(stringParams);
		}

		if (deletePartialAfterMerge != null) {
			handlebars.setDeletePartialAfterMerge(deletePartialAfterMerge);
		}

		if (infiniteLoops != null) {
			handlebars.setInfiniteLoops(infiniteLoops);
		}

		if (parentScopeResolution != null) {
			handlebars.setParentScopeResolution(parentScopeResolution);
		}

		if (prettyPrint != null) {
			handlebars.setPrettyPrint(prettyPrint);
		}

		// Apply customizers one by one
		if (customizers != null && !customizers.isEmpty()) {
			log.debug("Applying Handlebars Customizers...");
			for (HandlebarsCustomizer customizer : customizers) {
				log.debug(" -> Applying customizer: {}", customizer);
				customizer.customize(handlebars);
			}
		}

		return handlebars;
	}

	/**
	 * Set {@link #startDelimiter}
	 *
	 * @param startDelimiter New {@link #startDelimiter}
	 */
	public void setStartDelimiter(String startDelimiter) {
		this.startDelimiter = startDelimiter;
	}

	/**
	 * Set {@link #endDelimiter}
	 *
	 * @param endDelimiter New {@link #endDelimiter}
	 */
	public void setEndDelimiter(String endDelimiter) {
		this.endDelimiter = endDelimiter;
	}

	/**
	 * Set {@link #stringParams}
	 *
	 * @param stringParams New {@link #stringParams}
	 */
	public void setStringParams(boolean stringParams) {
		this.stringParams = stringParams;
	}

	/**
	 * Set {@link #deletePartialAfterMerge}
	 *
	 * @param deletePartialAfterMerge New {@link #deletePartialAfterMerge}
	 */
	public void setDeletePartialAfterMerge(boolean deletePartialAfterMerge) {
		this.deletePartialAfterMerge = deletePartialAfterMerge;
	}

	/**
	 * Set {@link #infiniteLoops}
	 *
	 * @param infiniteLoops New {@link #infiniteLoops}
	 */
	public void setInfiniteLoops(boolean infiniteLoops) {
		this.infiniteLoops = infiniteLoops;
	}

	/**
	 * Set {@link #parentScopeResolution}
	 *
	 * @param parentScopeResolution New {@link #parentScopeResolution}
	 */
	public void setParentScopeResolution(boolean parentScopeResolution) {
		this.parentScopeResolution = parentScopeResolution;
	}

	/**
	 * Set {@link #prettyPrint}
	 *
	 * @param prettyPrint New {@link #prettyPrint}
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	/**
	 * Set {@link #customizers}
	 *
	 * @param customizers New {@link #customizers}
	 */
	public void setCustomizers(Collection<HandlebarsCustomizer> customizers) {
		this.customizers = customizers;
	}
}
