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

package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static com.samskivert.mustache.Mustache.Compiler;

@Configuration
public class JMustacheConfiguration {

	private static final Logger log = LoggerFactory.getLogger(JMustacheConfiguration.class);

	private final Environment environment;

	@Autowired
	public JMustacheConfiguration(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Build mustache compiler.
	 * This compiler use an instance of {@link com.samskivert.mustache.Mustache.Compiler}
	 * under the hood.
	 *
	 * @param compiler JMustache compiler.
	 * @param templateLoader Template loader.
	 * @return Mustache compiler implementation.
	 */
	@Bean
	public MustacheCompiler mustacheCompiler(Mustache.Compiler compiler, MustacheTemplateLoader templateLoader) {
		log.info("Create JMustache compiler");
		return new JMustacheCompiler(compiler, templateLoader);
	}

	/**
	 * Build original jmustache {@link Compiler} that will be used
	 * internally to compile and render templates.
	 *
	 * @return JMustache compiler.
	 */
	@Bean
	public JMustacheCompilerFactoryBean jMustacheCompiler() {
		JMustacheCompilerFactoryBean factoryBean = new JMustacheCompilerFactoryBean();

		String nullValue = getNullValue();
		if (nullValue != null) {
			factoryBean.setNullValue(nullValue);
		}

		String defaultValue = getDefaultValue();
		if (defaultValue != null) {
			factoryBean.setDefaultValue(defaultValue);
		}

		Boolean emptyStringIsFalse = getEmptyStringIsFalse();
		if (emptyStringIsFalse != null) {
			factoryBean.setEmptyStringIsFalse(emptyStringIsFalse);
		}

		Boolean zeroIsFalse = getZeroIsFalse();
		if (zeroIsFalse != null) {
			factoryBean.setZeroIsFalse(zeroIsFalse);
		}

		Boolean escapeHTML = getEscapeHTML();
		if (escapeHTML != null) {
			factoryBean.setEscapeHTML(escapeHTML);
		}

		Boolean strictSections = getStrictSections();
		if (strictSections != null) {
			factoryBean.setStrictSections(strictSections);
		}

		Boolean standardsMode = getStandardsMode();
		if (standardsMode != null) {
			factoryBean.setStandardsMode(standardsMode);
		}

		return factoryBean;
	}

	private String getNullValue() {
		return environment.getProperty("mustache.jmustache.nullValue", String.class);
	}

	private String getDefaultValue() {
		return environment.getProperty("mustache.jmustache.defaultValue", String.class);
	}

	private Boolean getEmptyStringIsFalse() {
		return environment.getProperty("mustache.jmustache.emptyStringIsFalse", Boolean.class);
	}

	private Boolean getZeroIsFalse() {
		return environment.getProperty("mustache.jmustache.zeroIsFalse", Boolean.class);
	}

	private Boolean getEscapeHTML() {
		return environment.getProperty("mustache.jmustache.escapeHTML", Boolean.class);
	}

	private Boolean getStrictSections() {
		return environment.getProperty("mustache.jmustache.strictSections", Boolean.class);
	}

	private Boolean getStandardsMode() {
		return environment.getProperty("mustache.jmustache.standardsMode", Boolean.class);
	}
}
