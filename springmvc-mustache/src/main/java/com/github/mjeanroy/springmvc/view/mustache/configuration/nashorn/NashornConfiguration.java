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

package com.github.mjeanroy.springmvc.view.mustache.configuration.nashorn;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.github.mjeanroy.springmvc.view.mustache.nashorn.MustacheEngine;
import com.github.mjeanroy.springmvc.view.mustache.nashorn.NashornCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Create Nashorn beans used to render mustache templates.
 *
 * @deprecated Nashorn will be removed after jdk 11, so nashorn engine ill be removed in the next major version.
 */
@Configuration
@PropertySource(value = "classpath:mustache.properties", ignoreResourceNotFound = true)
@Deprecated
public class NashornConfiguration {

	private static final Logger log = LoggerFactory.getLogger(NashornConfiguration.class);

	private final Environment environment;

	@Autowired
	public NashornConfiguration(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public MustacheCompiler mustacheCompiler(MustacheTemplateLoader templateLoader, MustacheEngine mustacheEngine) {
		return new NashornCompiler(templateLoader, mustacheEngine);
	}

	@Bean
	public MustacheEngineFactoryBean mustacheEngine(MustacheTemplateLoader templateLoader) {
		MustacheEngineFactoryBean factoryBean = new MustacheEngineFactoryBean(templateLoader);

		// Try to get path from environment
		String path = getPath();
		if (path != null) {
			factoryBean.setPath(path);
		}

		return factoryBean;
	}

	private String getPath() {
		String deprecatedPath = environment.getProperty("mustache.path");
		if (deprecatedPath != null) {
			log.warn("Property `mustache.path` is deprecated, please use `mustache.nashorn.path` instead.");
			return deprecatedPath;
		}

		return environment.getProperty("mustache.nashorn.path");
	}
}
