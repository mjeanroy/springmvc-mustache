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

package com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.MustacheJavaCompiler;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheFactory;
import com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheResolver;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Collection;

import static java.util.Collections.emptyList;

@Configuration
public class MustacheJavaConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MustacheJavaConfiguration.class);

	private final Environment environment;

	private Collection<MustacheJavaCustomizer> customizers;

	@Autowired
	public MustacheJavaConfiguration(Environment environment) {
		this.environment = environment;
		this.customizers = emptyList();
	}

	@Autowired(required = false)
	public void setCustomizers(Collection<MustacheJavaCustomizer> customizers) {
		this.customizers = customizers;
	}

	/**
	 * Build mustache compiler.
	 *
	 * @param mustacheFactory The mustache factory to use.
	 * @param templateLoader Template loader.
	 * @return Mustache compiler implementation.
	 */
	@Bean
	public MustacheCompiler mustacheCompiler(MustacheFactory mustacheFactory, MustacheTemplateLoader templateLoader) {
		log.info("Create mustache.java compiler");
		return new MustacheJavaCompiler(mustacheFactory, templateLoader);
	}

	/**
	 * The mustache template resolver, use {@link MustacheTemplateLoader}.
	 *
	 * @param templateLoader Template loader.
	 * @return The template resolver.
	 */
	@Bean
	public MustacheResolver mustacheResolver(MustacheTemplateLoader templateLoader) {
		return new SpringMustacheResolver(templateLoader);
	}

	/**
	 * The mustache factory used to render mustache templates.
	 *
	 * @param mustacheResolver Template resolver.
	 * @param templateLoader Template loader.
	 * @return The mustache factory.
	 */
	@Bean
	public MustacheFactory mustacheFactory(MustacheResolver mustacheResolver, MustacheTemplateLoader templateLoader) {
		Integer recursionLimit = getRecursionLimit();

		log.debug("Creating mustache factory");
		log.debug(" - recursionLimit = {}", recursionLimit);

		SpringMustacheFactory factory = new SpringMustacheFactory(mustacheResolver, templateLoader);

		if (recursionLimit != null) {
			factory.setRecursionLimit(recursionLimit);
		}

		if (customizers != null && !customizers.isEmpty()) {
			log.debug("Applying mustache factory customizers");
			for (MustacheJavaCustomizer customizer : customizers) {
				log.debug(" - Applying mustache factory customizer: {}", customizer);
				customizer.customize(factory);
			}
		}

		return factory;
	}

	private Integer getRecursionLimit() {
		return environment.getProperty("mustache.mustachejava.recursionLimit", Integer.class);
	}
}
