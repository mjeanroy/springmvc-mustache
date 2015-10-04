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

import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

import static com.github.mjeanroy.springmvc.view.mustache.commons.ClassUtils.isPresent;
import static java.util.Collections.unmodifiableMap;

/**
 * Set of mustache provider.
 * A mustache provider is an implementation that can be used
 * to render mustache templates.
 */
public enum MustacheProvider {

	/**
	 * Mustache implementation that use jmustache as
	 * internal compiler.
	 */
	JMUSTACHE {
		@Override
		public Class configuration() {
			return JMustacheConfiguration.class;
		}
	},

	/**
	 * Mustache implementation that use handlebar java as
	 * internal compiler.
	 */
	HANDLEBARS {
		@Override
		public Class configuration() {
			return HandlebarsConfiguration.class;
		}
	},

	/**
	 * Mustache implementation that use mustache.java java as
	 * internal compiler.
	 */
	MUSTACHE_JAVA {
		@Override
		public Class configuration() {
			return MustacheJavaConfiguration.class;
		}
	},

	/**
	 * Option that detect class available on classpath
	 * and select the best implementation.
	 */
	AUTO {
		@Override
		public Class configuration() {
			for (Map.Entry<String, MustacheProvider> conf : CONF.entrySet()) {
				String klass = conf.getKey();
				MustacheProvider provider = conf.getValue();
				if (isPresent(klass)) {
					log.debug("Class '{}' found in classpath, use {} configuration", klass, provider.name());
					return provider.configuration();
				}
				else {
					log.trace("Class '{}' is missing, skip", klass);
				}
			}

			// No implementation detected, throw exception
			log.error("Mustache implementation is missing, please add one of following dependency to your classpath: {}", CONF.keySet());
			throw new IllegalArgumentException("Mustache implementation is missing, please add jmustache or handlebar to classpath");
		}
	};

	/**
	 * Get configuration class to import.
	 *
	 * @return Configuration class.
	 */
	public abstract Class configuration();

	private static final Map<String, MustacheProvider> CONF;

	static {
		Map<String, MustacheProvider> map = new TreeMap<String, MustacheProvider>();
		map.put("com.samskivert.mustache.Mustache", JMUSTACHE);
		map.put("com.github.jknack.handlebars.Handlebars", HANDLEBARS);
		map.put("com.github.mustachejava.MustacheFactory", MUSTACHE_JAVA);
		CONF = unmodifiableMap(map);
	}

	private static final Logger log = LoggerFactory.getLogger(MustacheProvider.class);

}
