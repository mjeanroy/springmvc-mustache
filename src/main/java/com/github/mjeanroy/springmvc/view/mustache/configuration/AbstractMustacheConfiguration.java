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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.CompositeResourceLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultMustacheTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Abstraction that create basic beans to use with
 * mustache template engine.
 */
@Configuration
public abstract class AbstractMustacheConfiguration {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractMustacheConfiguration.class);

	@Autowired(required = false)
	private ResourceLoader resourceLoader;

	@Autowired(required = false)
	private ApplicationContext applicationContext;

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
		log.debug("Build composite resource loader");
		Collection<ResourceLoader> resourceLoaders = new LinkedHashSet<ResourceLoader>();

		if (resourceLoader != null) {
			log.trace(" => Add custom resource loader: {}", resourceLoader);
			resourceLoaders.add(resourceLoader);
		}

		if (applicationContext != null) {
			log.trace(" => Add application context as resource loader: {}", applicationContext);
			resourceLoaders.add(applicationContext);
		}

		if (resourceLoaders.isEmpty()) {
			log.trace(" => Add instance of ClassPathXmlApplicationContext as resource loader");
			resourceLoaders.add(new ClassPathXmlApplicationContext());

			log.trace(" => Add instance of FileSystemXmlApplicationContext as resource loader");
			resourceLoaders.add(new FileSystemXmlApplicationContext());
		}

		log.debug("Create composite resource loader using: {}", resourceLoaders);
		log.trace(" => Number of loaders: {}", resourceLoaders.size());
		return new CompositeResourceLoader(resourceLoaders);
	}
}
