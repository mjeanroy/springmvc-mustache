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

/**
 * Set of default settings.
 */
public final class MustacheSettings {

	private MustacheSettings() {
	}

	/**
	 * Default prefix prepended to view names.
	 * This prefix will be used by view resolver and mustache template
	 * loader.
	 */
	public static final String PREFIX = "/templates/";

	/**
	 * Default suffix appended to view names.
	 * This suffix will be used by view resolver and mustache template
	 * loader.
	 */
	public static final String SUFFIX = ".template.html";

	/**
	 * Default view resolver order defined
	 * on {@link org.springframework.web.servlet.view.UrlBasedViewResolver#setOrder}.
	 * This settings set the order in which this {@link MustacheViewResolver}
	 * will be evaluated.
	 */
	public static final int ORDER = 1;

	/**
	 * Default cache settings defined
	 * on {@link org.springframework.web.servlet.view.UrlBasedViewResolver#setCache}.
	 */
	public static final boolean CACHE = true;

	/**
	 * Default cache settings defined
	 * on {@link org.springframework.web.servlet.view.UrlBasedViewResolver#setCache}.
	 * This settings set the view names (or name patterns) that can be handled by
	 * {@link MustacheViewResolver}
	 */
	public static final String VIEW_NAMES = "*";

	/**
	 * Default layout value
	 * on {@link com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver#setDefaultLayout(String)}.
	 * By default, this feature is disabled.
	 */
	public static final String DEFAULT_LAYOUT = "";

	/**
	 * Default key that can be used to map view to partials in default layout.
	 * This property is set on {@link com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver#setLayoutKey(String)}.
	 */
	public static final String LAYOUT_KEY = "content";

	/**
	 * Default key that can be used to define layout mappings.
	 * This property is set on {@link com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver#setLayoutMappings(java.util.Map)} (String)}.
	 */
	public static final String LAYOUT_MAPPINGS = "";

	/**
	 * Key used to store partials mapping in
	 * {@link org.springframework.web.servlet.ModelAndView} object.
	 * If mapping is defined, it will be automatically used during
	 * template compilation.
	 */
	public static final String PARTIALS_KEY = "$$partials$$";

	/**
	 * Default key that will be used to replace layout content
	 * with view to display.
	 */
	public static final String DEFAULT_LAYOUT_KEY = "content";
}
