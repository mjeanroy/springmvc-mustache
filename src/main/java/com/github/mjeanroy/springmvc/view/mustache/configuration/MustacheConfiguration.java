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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheTemplateLoader;
import com.samskivert.mustache.Mustache;

@Configuration
@PropertySource(
		value = "classpath:mustache.properties",
		ignoreResourceNotFound = true
)
public class MustacheConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MustacheConfiguration.class);

	public static final String PREFIX = "/templates/";

	public static final String SUFFIX = ".template.html";

	public static final int ORDER = 1;

	public static final boolean CACHE = false;

	@Autowired
	private Environment environment;

	@Bean
	public MustacheViewResolver mustacheViewResolver() {
		Compiler compiler = compiler();
		JMustacheTemplateLoader templateLoader = templateLoader();

		// Get resolver properties
		String prefix = getPrefix();
		String suffix = getSuffix();
		int order = getOrder();
		boolean cache = getCache();

		log.info("Build Mustache View Resolver");
		log.debug("- Prefix: {}", prefix);
		log.debug("- Suffix: {}", suffix);
		log.debug("- Order: {}", order);
		log.debug("- Cache: {}", cache);

		MustacheViewResolver resolver = new MustacheViewResolver(compiler, templateLoader);
		resolver.setCache(cache);
		resolver.setPrefix(prefix);
		resolver.setSuffix(suffix);
		resolver.setOrder(order);
		resolver.setViewNames(new String[]{
				"*.template.html",
				"*.mustache"
		});

		return resolver;
	}

	@Bean
	public Compiler compiler() {
		JMustacheTemplateLoader templateLoader = templateLoader();
		return Mustache.compiler()
				.nullValue("")
				.defaultValue("")
				.emptyStringIsFalse(true)
				.zeroIsFalse(true)
				.escapeHTML(true)
				.withLoader(templateLoader);
	}

	@Bean
	public JMustacheTemplateLoader templateLoader() {
		return new JMustacheTemplateLoader();
	}

	protected String getPrefix() {
		return environment.getProperty("mustache.prefix", PREFIX).trim();
	}

	protected String getSuffix() {
		return environment.getProperty("mustache.suffix", SUFFIX).trim();
	}

	protected int getOrder() {
		return parseInt(environment.getProperty("mustache.order", valueOf(ORDER)).trim());
	}

	protected boolean getCache() {
		return parseBoolean(environment.getProperty("mustache.cache", valueOf(CACHE)).trim());
	}
}
