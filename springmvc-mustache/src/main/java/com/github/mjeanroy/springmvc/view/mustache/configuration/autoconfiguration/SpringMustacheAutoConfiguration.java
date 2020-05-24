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

package com.github.mjeanroy.springmvc.view.mustache.configuration.autoconfiguration;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver;
import com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheEngineConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheTemplateLoaderConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheWebConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureBefore(MustacheAutoConfiguration.class)
public class SpringMustacheAutoConfiguration {

	@Configuration
	@AutoConfigureBefore(MustacheAutoConfiguration.class)
	@ConditionalOnMissingBean(MustacheTemplateLoader.class)
	@Import(MustacheTemplateLoaderConfiguration.class)
	public static class MustacheTemplateLoaderAutoConfiguration {
	}

	@Configuration
	@AutoConfigureBefore(MustacheAutoConfiguration.class)
	@ConditionalOnMissingBean(MustacheCompiler.class)
	@Import(MustacheEngineConfiguration.class)
	public static class MustacheEngineAutoConfiguration {
	}

	@Configuration
	@AutoConfigureBefore(MustacheAutoConfiguration.class)
	@ConditionalOnWebApplication
	@ConditionalOnMissingBean(MustacheViewResolver.class)
	@Import(MustacheWebConfiguration.class)
	public static class MustacheWebAutoConfiguration {
	}
}
