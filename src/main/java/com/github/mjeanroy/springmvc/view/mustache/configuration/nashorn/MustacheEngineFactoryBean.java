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

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.nashorn.MustacheEngine;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Create Nashorn Engine.
 */
public class MustacheEngineFactoryBean extends AbstractFactoryBean<MustacheEngine> implements FactoryBean<MustacheEngine> {

	/**
	 * Internal Loader.
	 */
	private final MustacheTemplateLoader templateLoader;

	/**
	 * Path to mustache javascript file.
	 */
	private String path;

	/**
	 * Create factory.
	 *
	 * @param templateLoader Template loader.
	 */
	public MustacheEngineFactoryBean(MustacheTemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	@Override
	public Class<?> getObjectType() {
		return MustacheEngine.class;
	}

	@Override
	protected MustacheEngine createInstance() throws Exception {
		return path == null ? new MustacheEngine(templateLoader) : new MustacheEngine(templateLoader, path);
	}

	/**
	 * Set {@link #path}
	 *
	 * @param path New {@link #path}
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
