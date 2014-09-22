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

import static com.github.mjeanroy.springmvc.view.mustache.commons.ClassUtils.isPresent;

import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebar.HandlebarConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaConfiguration;

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
	HANDLEBAR {
		@Override
		public Class configuration() {
			return HandlebarConfiguration.class;
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
			if (isPresent("com.samskivert.mustache.Mustache")) {
				// Use JMustache
				return JMUSTACHE.configuration();
			}

			if (isPresent("com.github.jknack.handlebars.Handlebars")) {
				// Use Handlebar
				return HANDLEBAR.configuration();
			}

			if (isPresent("com.github.mustachejava.MustacheFactory")) {
				// Use mustache.java
				return MUSTACHE_JAVA.configuration();
			}

			// No implementation detected, throw exception
			throw new IllegalArgumentException("Mustache implementation is missing, please add jmustache or handlebar to classpath");
		}
	};

	/**
	 * Get configuration class to import.
	 *
	 * @return Configuration class.
	 */
	public abstract Class configuration();

}
