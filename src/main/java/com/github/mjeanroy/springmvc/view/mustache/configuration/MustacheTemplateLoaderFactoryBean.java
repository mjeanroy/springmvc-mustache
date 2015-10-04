/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 <mickael.jeanroy@gmail.com>
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
import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Factory bean for {@link com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader} instance.
 */
public class MustacheTemplateLoaderFactoryBean extends AbstractFactoryBean<MustacheTemplateLoader> implements FactoryBean<MustacheTemplateLoader>, ApplicationContextAware, ResourceLoaderAware {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(MustacheTemplateLoaderFactoryBean.class);

	/**
	 * Current application context.
	 */
	private ApplicationContext applicationContext;

	/**
	 * Current resource loader.
	 */
	private ResourceLoader resourceLoader;

	/**
	 * @see {@link com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader#getPrefix()}
	 */
	private String prefix;

	/**
	 * @see {@link com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader#getSuffix()}
	 */
	private String suffix;

	/**
	 * @see {@link com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader#addPartialAliases(java.util.Map)}
	 */
	private final Map<String, String> partialAliases;

	/**
	 * Default constructor.
	 */
	public MustacheTemplateLoaderFactoryBean() {
		super();
		this.partialAliases = new HashMap<String, String>();
	}

	@Override
	public Class<?> getObjectType() {
		return MustacheTemplateLoader.class;
	}

	@Override
	protected MustacheTemplateLoader createInstance() throws Exception {
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
}