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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ResourceLoader;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.CompositeResourceLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultMustacheTemplateLoader;

/**
 * Abstraction that create basic beans to use with
 * mustache template engine.
 */
@Configuration
public abstract class AbstractMustacheConfiguration {

	private static final Logger log = LoggerFactory.getLogger(AbstractMustacheConfiguration.class);

	@Autowired(required = false)
	private ResourceLoader resourceLoader;

	@Autowired(required = false)
	private ApplicationContext applicationContext;

	/**
	 * Build mustache compiler.
	 * This compiler use an instance of {@link com.samskivert.mustache.Mustache.Compiler}
	 * under the hood.
	 *
	 * @return Mustache compiler implementation.
	 */
	@Bean
	public abstract MustacheCompiler mustacheCompiler();

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
}
