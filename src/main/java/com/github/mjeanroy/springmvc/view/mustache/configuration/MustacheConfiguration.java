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

import static com.samskivert.mustache.Mustache.Compiler;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultMustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler;
import com.samskivert.mustache.Mustache;

@Configuration
@PropertySource(
		value = "classpath:mustache.properties",
		ignoreResourceNotFound = true
)
public class MustacheConfiguration {

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
	 * This settings set the order in which this {@link com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver}
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
	 * {@link com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver}
	 */
	public static final String VIEW_NAMES = "*";

	@Autowired
	private Environment environment;

	/**
	 * Build mustache view resolver.
	 * This view resolver needs an instance of {@link com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler}
	 * to be created.
	 *
	 * @return Mustache view resolver instance.
	 */
	@Bean
	public MustacheViewResolver mustacheViewResolver() {
		MustacheCompiler compiler = mustacheCompiler();

		// Get resolver properties
		String prefix = getPrefix();
		String suffix = getSuffix();
		int order = getOrder();
		boolean cache = getCache();
		String[] viewNames = getViewNames();

		MustacheViewResolver resolver = new MustacheViewResolver(compiler);
		resolver.setCache(cache);
		resolver.setPrefix(prefix);
		resolver.setSuffix(suffix);
		resolver.setOrder(order);
		resolver.setViewNames(viewNames);
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
	public MustacheCompiler mustacheCompiler() {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader();
		Compiler jmustacheCompiler = Mustache.compiler()
				.nullValue("")
				.defaultValue("")
				.emptyStringIsFalse(true)
				.zeroIsFalse(true)
				.escapeHTML(true);

		return new JMustacheCompiler(jmustacheCompiler, templateLoader);
	}

	/**
	 * Build mustache template loader.
	 * This compiler use an instance of {@link org.springframework.core.io.DefaultResourceLoader}
	 * under the hood.
	 *
	 * @return Mustache template loader implementation.
	 */
	@Bean
	public MustacheTemplateLoader mustacheTemplateLoader() {
		return new DefaultMustacheTemplateLoader();
	}

	/**
	 * Resolve views prefix value.
	 * Default is to look for "mustache.prefix" property or use {@link #PREFIX} if
	 * property cannot be resolved.
	 *
	 * @return Prefix value.
	 */
	protected String getPrefix() {
		return environment.getProperty("mustache.prefix", PREFIX).trim();
	}

	/**
	 * Resolve views suffix value.
	 * Default is to look for "mustache.suffix" property or use {@link #SUFFIX} if
	 * property cannot be resolved.
	 *
	 * @return Suffix value.
	 */
	protected String getSuffix() {
		return environment.getProperty("mustache.suffix", SUFFIX).trim();
	}

	/**
	 * Resolve mustache view resolver order.
	 * Default is to look for "mustache.order" property or use {@link #ORDER} if
	 * property cannot be resolved.
	 *
	 * @return Order value.
	 */
	protected int getOrder() {
		return parseInt(environment.getProperty("mustache.order", valueOf(ORDER)).trim());
	}

	/**
	 * Resolve mustache view resolver cache settings.
	 * Default is to look for "mustache.cache" property or use {@link #CACHE} if
	 * property cannot be resolved.
	 *
	 * @return Cache settings.
	 */
	protected boolean getCache() {
		return parseBoolean(environment.getProperty("mustache.cache", valueOf(CACHE)).trim());
	}

	/**
	 * Resolve view names matchers of mustache view resolver.
	 * Default is to look for "mustache.viewNames" property or use {@link #VIEW_NAMES} if
	 * property cannot be resolved.
	 *
	 * @return View names patterns.
	 */
	protected String[] getViewNames() {
		String viewNames = environment.getProperty("mustache.viewNames", valueOf(VIEW_NAMES)).trim();
		String[] names = viewNames.split(",");
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].trim();
		}
		return names;
	}
}
