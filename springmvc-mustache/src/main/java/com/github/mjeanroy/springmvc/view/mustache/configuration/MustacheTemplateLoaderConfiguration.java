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

import com.github.mjeanroy.springmvc.view.mustache.MustacheSettings;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Abstraction that create basic beans to use with
 * mustache template engine.
 */
@Configuration
public class MustacheTemplateLoaderConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MustacheTemplateLoaderConfiguration.class);

	private final Environment environment;

	@Autowired
	public MustacheTemplateLoaderConfiguration(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Build mustache template loader.
	 * This compiler use an instance of {@link org.springframework.core.io.DefaultResourceLoader}
	 * under the hood.
	 *
	 * @return Mustache template loader implementation.
	 */
	@Bean
	public MustacheTemplateLoaderFactoryBean mustacheTemplateLoader() {
		log.info("Create default mustache template loader");
		MustacheTemplateLoaderFactoryBean factoryBean = new MustacheTemplateLoaderFactoryBean();
		factoryBean.setPrefix(getPrefix());
		factoryBean.setSuffix(getSuffix());
		return factoryBean;
	}

	/**
	 * Resolve views prefix value.
	 * Default is to look for "mustache.prefix" property or use {@link com.github.mjeanroy.springmvc.view.mustache.MustacheSettings#PREFIX} if
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
}
