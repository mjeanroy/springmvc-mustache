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

package com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.handlebars.HandlebarsCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure handlebar template engine.
 */
@Configuration
public class HandlebarsConfiguration {

	private static final Logger log = LoggerFactory.getLogger(HandlebarsConfiguration.class);

	/**
	 * Build mustache compiler.
	 * This compiler use an instance of {@link com.github.jknack.handlebars.Handlebars}
	 * under the hood.
	 *
	 * @param handlebars Handlebars instance.
	 * @param templateLoader Template loader.
	 * @return Mustache compiler implementation.
	 */
	@Bean
	public MustacheCompiler mustacheCompiler(Handlebars handlebars, MustacheTemplateLoader templateLoader) {
		log.info("Create handlebar compiler");
		return new HandlebarsCompiler(handlebars, templateLoader);
	}

	/**
	 * Build original {@link com.github.jknack.handlebars.Handlebars} compiler
	 * that will be used to compile and render templates.
	 *
	 * @return Handlebars compiler.
	 */
	@Bean
	public HandlebarsFactoryBean handlebarsCompiler() {
		return new HandlebarsFactoryBean();
	}
}
