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

package com.github.mjeanroy.springmvc.view.mustache;

import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.hasText;
import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notNull;

/**
 * Mustache View Resolver.
 */
public class MustacheViewResolver extends AbstractTemplateViewResolver {

	private static final Logger log = LoggerFactory.getLogger(MustacheViewResolver.class);

	/**
	 * Mustache compiler.
	 */
	private final MustacheCompiler compiler;

	/**
	 * Main layout that can be used to define view layouts.
	 * This layout will be used by default for each views if it has been set.
	 */
	// Volatile because it can be accessed from more than one thread
	private volatile String defaultLayout;

	/**
	 * Key to use to define layout content.
	 * For example, with a layout template defined as (using a key equal to 'content'):
	 *
	 * <div>
	 *   Header
	 *   {{> content}}
	 *   Footer
	 * </div>
	 *
	 * Each view will replace 'content' partials.
	 */
	// Volatile because it can be accessed from more than one thread
	private volatile String layoutKey;

	/**
	 * Layout mappings that can be used to map different layout for different views.
	 * If mapping is not found and a default layout is defined, default layout will be used.
	 */
	private final Map<String, String> layoutMappings;

	/**
	 * Build new mustache resolver using compiler
	 *
	 * @param compiler Mustache compiler.
	 */
	public MustacheViewResolver(MustacheCompiler compiler) {
		setViewClass(requiredViewClass());

		this.compiler = notNull(compiler, "Compiler must not be null");
		this.layoutKey = MustacheSettings.DEFAULT_LAYOUT_KEY;
		this.layoutMappings = new HashMap<String, String>();
	}

	@Override
	protected Class<?> requiredViewClass() {
		return MustacheView.class;
	}

	@Override
	public void setPrefix(String prefix) {
		log.trace("Set view resolver prefix: '{}'", prefix);

		super.setPrefix(prefix);
		compiler.setPrefix(prefix);
	}

	@Override
	public void setSuffix(String suffix) {
		log.trace("Set view resolver suffix: '{}'", suffix);

		super.setSuffix(suffix);
		compiler.setSuffix(suffix);
	}

	/**
	 * Update default layout view.
	 *
	 * @param defaultLayout Default layout.
	 */
	public void setDefaultLayout(String defaultLayout) {
		log.trace("Set view resolver default layout: '{}'", defaultLayout);
		this.defaultLayout = hasText(defaultLayout, "Default layout must not be empty");
	}

	/**
	 * Update view layout key.
	 *
	 * @param layoutKey New view layout key.
	 */
	public void setLayoutKey(String layoutKey) {
		log.trace("Set view resolver layout key: '{}'", layoutKey);
		this.layoutKey = hasText(layoutKey, "Layout key must not be empty");
	}

	/**
	 * Replace current layout mappings by new mappings.
	 *
	 * @param layoutMappings New mappings.
	 */
	public void setLayoutMappings(Map<String, String> layoutMappings) {
		notNull(layoutMappings, "Layout mappings must not be null");

		log.debug("Set view resolver layout mappings");

		this.layoutMappings.clear();
		for (Map.Entry<String, String> entry : layoutMappings.entrySet()) {
			addLayoutMapping(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Replace current layout mappings by new mappings.
	 *
	 * @param viewName View name to map.
	 * @param layoutName Layout name to use for given view.
	 */
	public void addLayoutMapping(String viewName, String layoutName) {
		log.debug("Add new layout mapping");
		log.trace("  => {} -> {}", viewName, layoutName);

		notNull(viewName, "View name must not be null");
		notNull(layoutName, "Layout name must not be null");
		this.layoutMappings.put(viewName, layoutName);
	}

	@Override
	protected MustacheView buildView(String viewName) throws Exception {
		final String mapping = layoutMappings.get(viewName);
		final String layout = mapping != null ? mapping : defaultLayout;
		final boolean useLayout = layout != null && layoutKey != null;
		final String name = useLayout ? layout : viewName;

		log.info("Build view '{}'", viewName);
		log.trace("  => Use layout: {}", useLayout);
		log.trace("  => Layout: {}", layout);
		log.trace("  => Name: {}", name);

		final MustacheView view = (MustacheView) super.buildView(name);
		view.setCompiler(compiler);

		if (useLayout) {
			// Add alias to map main content to real view
			view.addAlias(layoutKey, viewName);
		}

		return view;
	}
}

