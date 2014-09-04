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

import static com.samskivert.mustache.Mustache.Compiler;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheTemplateLoader;

/**
 * Mustache View Resolver.
 */
public class MustacheViewResolver extends AbstractTemplateViewResolver {

	public static final String DEFAULT_LAYOUT_KEY = "content";

	/** Mustache compiler. */
	private final Compiler compiler;

	/** Template Loader. */
	private final JMustacheTemplateLoader templateLoader;

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
	 * @param templateLoader Mustache template loader.
	 */
	public MustacheViewResolver(Compiler compiler, JMustacheTemplateLoader templateLoader) {
		notNull(compiler);
		notNull(templateLoader);

		setViewClass(requiredViewClass());
		this.compiler = compiler;
		this.templateLoader = templateLoader;
		this.layoutKey = DEFAULT_LAYOUT_KEY;
		this.layoutMappings = new HashMap<String, String>();
	}

	/**
	 * Build new mustache resolver using compiler
	 *
	 * @param compiler Mustache compiler.
	 * @param resourceLoader Spring resource loader.
	 */
	public MustacheViewResolver(Compiler compiler, ResourceLoader resourceLoader) {
		notNull(compiler);
		notNull(resourceLoader);

		setViewClass(requiredViewClass());
		this.compiler = compiler;
		this.templateLoader = new JMustacheTemplateLoader(resourceLoader);
		this.layoutKey = DEFAULT_LAYOUT_KEY;
		this.layoutMappings = new HashMap<String, String>();
	}

	@Override
	protected Class<?> requiredViewClass() {
		return MustacheView.class;
	}

	@Override
	public void setPrefix(String prefix) {
		super.setPrefix(prefix);
		templateLoader.setPrefix(prefix);
	}

	@Override
	public void setSuffix(String suffix) {
		super.setSuffix(suffix);
		templateLoader.setSuffix(suffix);
	}

	/**
	 * Update default layout view.
	 *
	 * @param defaultLayout Default layout.
	 */
	public void setDefaultLayout(String defaultLayout) {
		hasText(defaultLayout);
		this.defaultLayout = defaultLayout;
	}

	/**
	 * Update view layout key.
	 *
	 * @param layoutKey New view layout key.
	 */
	public void setLayoutKey(String layoutKey) {
		hasText(layoutKey);
		this.layoutKey = layoutKey;
	}

	/**
	 * Replace current layout mappings by new mappings.
	 *
	 * @param layoutMappings New mappings.
	 */
	public void setLayoutMappings(Map<String, String> layoutMappings) {
		notNull(layoutMappings);
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
		notNull(viewName);
		notNull(layoutName);
		this.layoutMappings.put(viewName, layoutName);
	}

	@Override
	protected MustacheView buildView(String viewName) throws Exception {
		final String mapping = layoutMappings.get(viewName);
		final String layout = mapping != null ? mapping : defaultLayout;
		final boolean useLayout = layout != null && layoutKey != null;
		final String name = useLayout ? layout : viewName;

		final MustacheView view = (MustacheView) super.buildView(name);
		view.setCompiler(compiler);
		view.setTemplateLoader(templateLoader);

		if (useLayout) {
			// Add alias to map main content to real view
			view.addAlias(layoutKey, viewName);
		}

		return view;
	}
}

