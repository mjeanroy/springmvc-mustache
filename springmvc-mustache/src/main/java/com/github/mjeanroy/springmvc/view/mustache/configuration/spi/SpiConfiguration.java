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

package com.github.mjeanroy.springmvc.view.mustache.configuration.spi;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheCompilerProviderException;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Configuration
public class SpiConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SpiConfiguration.class);

	@Bean
	public MustacheCompiler mustacheCompiler(MustacheTemplateLoader templateLoader) {
		List<MustacheCompilerProvider> providers = new ArrayList<>();
		for (MustacheCompilerProvider provider : ServiceLoader.load(MustacheCompilerProvider.class)) {
			providers.add(provider);
		}

		if (providers.isEmpty()) {
			throw new MustacheCompilerProviderException(
					"No implementation of " + MustacheCompilerProvider.class + " found, please register one (see: https://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html)"
			);
		}

		if (providers.size() > 1 && log.isWarnEnabled()) {
			log.warn("Found more than one registered implementations for {}", MustacheCompilerProvider.class);
			for (MustacheCompilerProvider provider : providers) {
				log.warn(" - Found: {}", provider);
			}
		}

		return providers.get(0).mustacheCompiler(templateLoader);
	}
}
