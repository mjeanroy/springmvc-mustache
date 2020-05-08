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

package com.github.mjeanroy.springmvc.view.mustache.core;

import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notEmpty;
import static com.github.mjeanroy.springmvc.view.mustache.commons.PreConditions.notNull;

/**
 * Implementation of spring {@link org.springframework.core.io.ResourceLoader}
 * that will use internally a set of resource loaders.
 * When a resource is requested, each resources loaders will be queried until one find
 * an existing resources.
 * If no one find an existing resource, the last computed resource is returned.
 */
public class CompositeResourceLoader implements ResourceLoader {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(CompositeResourceLoader.class);

	/**
	 * Set of resource loaders that will be used internally.
	 */
	private final List<ResourceLoader> resourceLoaders;

	/**
	 * Create new composite resource loader.
	 *
	 * @param resourceLoaders Collection of resource loaders.
	 */
	public CompositeResourceLoader(Collection<ResourceLoader> resourceLoaders) {
		notNull(resourceLoaders, "Resource loaders must not be null");
		notEmpty(resourceLoaders, "Resource loaders must not be empty");
		this.resourceLoaders = new ArrayList<ResourceLoader>(resourceLoaders);
	}

	@Override
	public Resource getResource(String location) {
		log.debug("Get resource: {}", location);

		Resource resource = null;

		for (ResourceLoader resourceLoader : resourceLoaders) {
			log.trace("Test for resourceLoader: {}", resourceLoader);
			resource = resourceLoader.getResource(location);

			if (resource.exists()) {
				log.trace("Resource {} exist, return it", resource);
				return resource;
			}
		}

		// Return last resource
		log.trace("Resource {} not found, return last computed value", resource);
		return resource;
	}

	@Override
	public ClassLoader getClassLoader() {
		return ClassUtils.getDefaultClassLoader();
	}
}
