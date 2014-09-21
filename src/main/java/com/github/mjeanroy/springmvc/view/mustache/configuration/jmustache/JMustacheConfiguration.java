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

package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.configuration.AbstractMustacheConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler;
import com.samskivert.mustache.Mustache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.samskivert.mustache.Mustache.Compiler;

@Configuration
public class JMustacheConfiguration extends AbstractMustacheConfiguration {

	/**
	 * Build mustache compiler.
	 * This compiler use an instance of {@link com.samskivert.mustache.Mustache.Compiler}
	 * under the hood.
	 *
	 * @return Mustache compiler implementation.
	 */
	@Bean
	public MustacheCompiler mustacheCompiler() {
		return new JMustacheCompiler(jMustacheCompiler(), mustacheTemplateLoader());
	}

	/**
	 * Build original jmustache {@link Compiler} that will be used
	 * internally to compile and render templates.
	 *
	 * @return JMustache compiler.
	 */
	@Bean
	public Compiler jMustacheCompiler() {
		return Mustache.compiler()
				.nullValue("")
				.defaultValue("")
				.emptyStringIsFalse(true)
				.zeroIsFalse(true)
				.escapeHTML(true);
	}
}
