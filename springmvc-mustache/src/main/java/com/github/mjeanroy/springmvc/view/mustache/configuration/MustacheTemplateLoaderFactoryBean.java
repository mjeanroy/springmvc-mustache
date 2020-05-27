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

package com.github.mjeanroy.springmvc.view.mustache.configuration;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.commons.lang.ToStringBuilder;
import com.github.mjeanroy.springmvc.view.mustache.core.CompositeResourceLoader;
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.hasText;
import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.notNull;

/**
 * Factory bean for {@link com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader} instance.
 */
public class MustacheTemplateLoaderFactoryBean extends AbstractFactoryBean<MustacheTemplateLoader> implements FactoryBean<MustacheTemplateLoader>, ApplicationContextAware, ResourceLoaderAware {

	private static final Logger log = LoggerFactory.getLogger(MustacheTemplateLoaderFactoryBean.class);

	/**
	 * Classpath resource loader, implemented as a singleton.
	 */
	private static final ClasspathResourceLoader CLASSPATH_RESOURCE_LOADER = new ClasspathResourceLoader();

	/**
	 * Current application context.
	 */
	private ApplicationContext applicationContext;

	/**
	 * Current resource loader.
	 */
	private ResourceLoader resourceLoader;

	/**
	 * @see com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader#getPrefix()
	 */
	private String prefix;

	/**
	 * @see com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader#getSuffix()
	 */
	private String suffix;

	/**
	 * @see com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader#addPartialAliases(java.util.Map)
	 */
	private final Map<String, String> partialAliases;

	/**
	 * Default constructor.
	 */
	public MustacheTemplateLoaderFactoryBean() {
		super();
		this.partialAliases = new HashMap<>();
	}

	@Override
	public Class<?> getObjectType() {
		return MustacheTemplateLoader.class;
	}

	@Override
	protected MustacheTemplateLoader createInstance() {
		log.debug("Create instance of {}", DefaultTemplateLoader.class);
		DefaultTemplateLoader templateLoader = new DefaultTemplateLoader(computeResourceLoader());
		templateLoader.setPrefix(prefix);
		templateLoader.setSuffix(suffix);
		templateLoader.addPartialAliases(partialAliases);
		return templateLoader;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	protected ResourceLoader computeResourceLoader() {
		log.debug("Build composite resource loader");
		Collection<ResourceLoader> resourceLoaders = new LinkedHashSet<>();

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

		log.debug("Add instance of classpath resource loader");
		resourceLoaders.add(CLASSPATH_RESOURCE_LOADER);

		log.debug("Create composite resource loader using: {}", resourceLoaders);
		log.trace(" => Number of loaders: {}", resourceLoaders.size());
		return new CompositeResourceLoader(resourceLoaders);
	}

	/**
	 * Set {@link #prefix}
	 *
	 * @param prefix New {@link #prefix}
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Set {@link #suffix}
	 *
	 * @param suffix New {@link #suffix}
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Set {@link #partialAliases}
	 *
	 * @param partialAliases New {@link #partialAliases}
	 */
	public void setPartialAliases(Map<String, String> partialAliases) {
		this.partialAliases.putAll(partialAliases);
	}

	/**
	 * Implementation of spring {@link org.springframework.core.io.ResourceLoader}
	 * that will always check for resources in the classpath (not the root of the application
	 * context).
	 *
	 * This class does not guarantee that resource patterns such as "file:/" or "http:" will
	 * work, that's why this class should remain private and should not be used outside.
	 */
	private static final class ClasspathResourceLoader implements ResourceLoader {

		/**
		 * Classpath prefix.
		 */
		private static final String CLASSPATH_PREFIX = "classpath:";

		/**
		 * Create new resource loader.
		 */
		private ClasspathResourceLoader() {
		}

		@Override
		public Resource getResource(String location) {
			notNull(location, "Resource location must not be null");
			hasText(location, "Resource location must be defined");

			// Remove the classpath if specified.
			// This should never happen since a previous resource loader will probably be used
			// before.
			String classpathLocation = location;
			if (location.startsWith(CLASSPATH_PREFIX)) {
				classpathLocation = location.substring(CLASSPATH_PREFIX.length());
			}

			return new ClassPathResource(classpathLocation);
		}

		@Override
		public ClassLoader getClassLoader() {
			return ClassUtils.getDefaultClassLoader();
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.builder(this)
				.append("resourceLoader", resourceLoader)
				.append("prefix", prefix)
				.append("suffix", suffix)
				.append("applicationContext", applicationContext)
				.build();
	}
}
