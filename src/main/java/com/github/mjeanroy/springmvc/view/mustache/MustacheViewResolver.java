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

package com.github.mjeanroy.springmvc.view.mustache;

import static com.samskivert.mustache.Mustache.Compiler;
import static org.springframework.util.Assert.notNull;

import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheTemplateLoader;

/**
 * Mustache View Resolver.
 */
public class MustacheViewResolver extends AbstractTemplateViewResolver {

	/** Mustache compiler. */
	private final Compiler compiler;

	/** Template Loader. */
	private final JMustacheTemplateLoader templateLoader;

	/**
	 * Build new mustache resolver using compiler
	 *
	 * @param compiler Mustache compiler.
	 * @param templateLoader Mustache template loader.
	 */
	public MustacheViewResolver(Compiler compiler, JMustacheTemplateLoader templateLoader) {
		notNull(compiler);
		notNull(templateLoader);

		setViewClass(requiredViewClass());
		this.compiler = compiler;
		this.templateLoader = templateLoader;
	}

	/**
	 * Build new mustache resolver using compiler
	 *
	 * @param compiler Mustache compiler.
	 * @param resourceLoader Spring resource loader.
	 */
	public MustacheViewResolver(Compiler compiler, ResourceLoader resourceLoader) {
		notNull(compiler);
		notNull(resourceLoader);

		setViewClass(requiredViewClass());
		this.compiler = compiler;
		this.templateLoader = new JMustacheTemplateLoader(resourceLoader);
	}

	@Override
	protected Class<?> requiredViewClass() {
		return MustacheView.class;
	}

	@Override
	public void setPrefix(String prefix) {
		super.setPrefix(prefix);
		templateLoader.setPrefix(prefix);
	}

	@Override
	public void setSuffix(String suffix) {
		super.setSuffix(suffix);
		templateLoader.setSuffix(suffix);
	}

	@Override
	protected MustacheView buildView(String viewName) throws Exception {
		MustacheView view = (MustacheView) super.buildView(viewName);
		view.setCompiler(compiler);
		view.setTemplateLoader(templateLoader);
		return view;
	}
}

