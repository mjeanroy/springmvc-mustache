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

package com.github.mjeanroy.springmvc.view.mustache.configuration;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheSettings;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver;
import com.github.mjeanroy.springmvc.view.mustache.core.CompositeResourceLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultMustacheTemplateLoader;

/**
 * Abstraction that create basic beans to use with
 * mustache template engine.
 */
@Configuration
@PropertySource(
		value = "classpath:mustache.properties",
		ignoreResourceNotFound = true
)
public abstract class AbstractMustacheConfiguration {

	private static final Logger log = LoggerFactory.getLogger(AbstractMustacheConfiguration.class);

	@Autowired
	private Environment environment;

	@Autowired(required = false)
	private ResourceLoader resourceLoader;

	@Autowired(required = false)
	private ApplicationContext applicationContext;

	/**
	 * Build mustache view resolver.
	 * This view resolver needs an instance of {@link com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler}
	 * to be created.
	 *
	 * @return Mustache view resolver instance.
	 */
	@Bean
	public MustacheViewResolver mustacheViewResolver(MustacheCompiler compiler) {
		// Get resolver properties
		String prefix = getPrefix();
		String suffix = getSuffix();
		int order = getOrder();
		boolean cache = getCache();
		String[] viewNames = getViewNames();
		String defaultLayout = getDefaultLayout();
		String layoutKey = getLayoutKey();
		Map<String, String> mappings = getLayoutMappings();

		log.info("Create mustache view resolver");
		log.trace("  => Cache: {}", cache);
		log.trace("  => Prefix: {}", prefix);
		log.trace("  => Suffix: {}", suffix);
		log.trace("  => Order: {}", order);
		log.trace("  => View Names: {}", viewNames);
		log.trace("  => Default layout: {}", defaultLayout);
		log.trace("  => Layout key: {}", layoutKey);
		log.trace("  => Mappings: {}", mappings);

		MustacheViewResolver resolver = new MustacheViewResolver(compiler);
		resolver.setCache(cache);
		resolver.setPrefix(prefix);
		resolver.setSuffix(suffix);
		resolver.setOrder(order);
		resolver.setViewNames(viewNames);
		resolver.setLayoutKey(layoutKey);

		if (defaultLayout != null && !defaultLayout.isEmpty()) {
			resolver.setDefaultLayout(defaultLayout);
		}

		if (!mappings.isEmpty()) {
			resolver.setLayoutMappings(mappings);
		}

		return resolver;
	}

	/**
	 * Build mustache compiler.
	 * This compiler use an instance of {@link com.samskivert.mustache.Mustache.Compiler}
	 * under the hood.
	 *
	 * @return Mustache compiler implementation.
	 */
	@Bean
	public abstract MustacheCompiler mustacheCompiler(MustacheTemplateLoader mustacheTemplateLoader);

	/**
	 * Build mustache template loader.
	 * This compiler use an instance of {@link org.springframework.core.io.DefaultResourceLoader}
	 * under the hood.
	 *
	 * @return Mustache template loader implementation.
	 */
	@Bean
	public MustacheTemplateLoader mustacheTemplateLoader() {
		log.info("Create default mustache template loader");
		return new DefaultMustacheTemplateLoader(getResourceLoader());
	}

	/**
	 * Get active resource loader.
	 * The default implementation is to return a set of resource loaders.
	 *
	 * @return Resource loader.
	 */
	protected ResourceLoader getResourceLoader() {
		List<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();

		if (resourceLoader != null) {
			resourceLoaders.add(resourceLoader);
		}

		if (applicationContext != null) {
			resourceLoaders.add(applicationContext);
		}

		if (resourceLoaders.isEmpty()) {
			resourceLoaders.add(new ClassPathXmlApplicationContext());
			resourceLoaders.add(new FileSystemXmlApplicationContext());
		}

		log.debug("Create resource loader using: {}", resourceLoaders);

		return new CompositeResourceLoader(resourceLoaders);
	}

	/**
	 * Resolve views prefix value.
	 * Default is to look for "mustache.prefix" property or use {@link MustacheSettings#PREFIX} if
	 * property cannot be resolved.
	 *
	 * @return Prefix value.
	 */
	public String getPrefix() {
		return environment.getProperty("mustache.prefix", MustacheSettings.PREFIX).trim();
	}

	/**
	 * Resolve views suffix value.
	 * Default is to look for "mustache.suffix" property or use {@link MustacheSettings#SUFFIX} if
	 * property cannot be resolved.
	 *
	 * @return Suffix value.
	 */
	public String getSuffix() {
		return environment.getProperty("mustache.suffix", MustacheSettings.SUFFIX).trim();
	}

	/**
	 * Resolve mustache view resolver order.
	 * Default is to look for "mustache.order" property or use {@link MustacheSettings#ORDER} if
	 * property cannot be resolved.
	 *
	 * @return Order value.
	 */
	public int getOrder() {
		return parseInt(environment.getProperty("mustache.order", valueOf(MustacheSettings.ORDER)).trim());
	}

	/**
	 * Resolve mustache view resolver cache settings.
	 * Default is to look for "mustache.cache" property or use {@link MustacheSettings#CACHE} if
	 * property cannot be resolved.
	 *
	 * @return Cache settings.
	 */
	public boolean getCache() {
		return parseBoolean(environment.getProperty("mustache.cache", valueOf(MustacheSettings.CACHE)).trim());
	}

	/**
	 * Resolve view names matchers of mustache view resolver.
	 * Default is to look for "mustache.viewNames" property or use {@link MustacheSettings#VIEW_NAMES} if
	 * property cannot be resolved.
	 *
	 * @return View names patterns.
	 */
	public String[] getViewNames() {
		String viewNames = environment.getProperty("mustache.viewNames", valueOf(MustacheSettings.VIEW_NAMES)).trim();
		String[] names = viewNames.split(",");
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].trim();
		}
		return names;
	}

	/**
	 * Resolve default layout to use.
	 * This layout can be used to define template to be used as main layout and render
	 * view within this layout.
	 *
	 * @return Layout name.
	 */
	public String getDefaultLayout() {
		return environment.getProperty("mustache.defaultLayout", MustacheSettings.DEFAULT_LAYOUT).trim();
	}

	/**
	 * Resolve default key to use as partials alias in default view layout.
	 *
	 * @return Partial key in layout.
	 */
	public String getLayoutKey() {
		return environment.getProperty("mustache.layoutKey", MustacheSettings.LAYOUT_KEY).trim();
	}

	/**
	 * Get mappings to use with resolvers.
	 *
	 * @return Layouts mappings
	 */
	public Map<String, String> getLayoutMappings() {
		String mappingsValues = environment.getProperty("mustache.layoutMappings", MustacheSettings.LAYOUT_MAPPINGS).trim();

		if (mappingsValues.isEmpty()) {
			return emptyMap();
		}

		Map<String, String> mappings = new HashMap<String, String>();
		String[] values = mappingsValues.split(";");
		if (values.length > 0) {
			for (String value : values) {
				String val = value == null ? "" : value.trim();
				if (val.isEmpty()) {
					continue;
				}

				String[] mapping = val.split(":");
				if (mapping.length != 2) {
					throw new IllegalArgumentException("Mapping must use [viewName]:[layout] format!");
				}

				mappings.put(mapping[0].trim(), mapping[1].trim());
			}
		}

		return unmodifiableMap(mappings);
	}
}
