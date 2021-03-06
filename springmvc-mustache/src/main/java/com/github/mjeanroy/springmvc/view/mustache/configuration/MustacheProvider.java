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

package com.github.mjeanroy.springmvc.view.mustache.configuration;

import com.github.jknack.handlebars.Handlebars;
import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsCustomizer;
import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsFactoryBean;
import com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheCompilerFactoryBean;
import com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.configuration.spi.MustacheCompilerProvider;
import com.github.mjeanroy.springmvc.view.mustache.configuration.spi.SpiConfiguration;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheResolver;
import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.ServiceLoader;

import static com.github.mjeanroy.springmvc.view.mustache.commons.reflection.Classes.isPresent;
import static java.util.Arrays.sort;

/**
 * Set of mustache provider.
 *
 * A mustache provider is an implementation that can be used
 * to render mustache templates.
 */
public enum MustacheProvider {

	/**
	 * Mustache implementation that use implementation registered using the ServiceProvider Interface added
	 * with jdk 1.6.
	 */
	SPI {
		@Override
		public boolean isAvailable() {
			return ServiceLoader.load(MustacheCompilerProvider.class).iterator().hasNext();
		}

		@Override
		public String configurationClass() {
			return SpiConfiguration.class.getName();
		}

		@Override
		MustacheCompiler doInstantiate(ApplicationContext applicationContext, Environment environment, MustacheTemplateLoader templateLoader) {
			return new SpiConfiguration().mustacheCompiler(templateLoader);
		}
	},

	/**
	 * Mustache implementation that use jmustache as
	 * internal compiler.
	 */
	JMUSTACHE {
		@Override
		public boolean isAvailable() {
			return isPresent("com.samskivert.mustache.Mustache");
		}

		@Override
		public String configurationClass() {
			return JMustacheConfiguration.class.getName();
		}

		@Override
		MustacheCompiler doInstantiate(ApplicationContext applicationContext, Environment environment, MustacheTemplateLoader templateLoader) throws Exception {
			Mustache.Compiler compiler = compiler(applicationContext, environment);
			return new JMustacheConfiguration(environment).mustacheCompiler(compiler, templateLoader);
		}

		/**
		 * The JMustache compiler.
		 *
		 * @param applicationContext The application context.
		 * @param environment Application context environment.
		 * @return The compiler.
		 */
		private Mustache.Compiler compiler(ApplicationContext applicationContext, Environment environment) throws Exception {
			try {
				return applicationContext.getBean(Mustache.Compiler.class);
			}
			catch (NoSuchBeanDefinitionException ex) {
				log.warn(ex.getMessage());
				JMustacheConfiguration jMustacheConfiguration = new JMustacheConfiguration(environment);
				JMustacheCompilerFactoryBean jMustacheCompilerFactoryBean = jMustacheConfiguration.jMustacheCompiler();
				jMustacheCompilerFactoryBean.afterPropertiesSet();
				return jMustacheCompilerFactoryBean.getObject();
			}
		}
	},

	/**
	 * Mustache implementation that use handlebar java as
	 * internal compiler.
	 */
	HANDLEBARS {
		@Override
		public boolean isAvailable() {
			return isPresent("com.github.jknack.handlebars.Handlebars");
		}

		@Override
		public String configurationClass() {
			return HandlebarsConfiguration.class.getName();
		}

		@Override
		MustacheCompiler doInstantiate(ApplicationContext applicationContext, Environment environment, MustacheTemplateLoader templateLoader) throws Exception {
			Collection<HandlebarsCustomizer> customizers = handlebarsCustomizers(applicationContext);
			Handlebars handlebars = handlebars(applicationContext, environment, customizers);

			HandlebarsConfiguration configuration = new HandlebarsConfiguration(environment);
			configuration.setCustomizers(customizers);
			return configuration.mustacheCompiler(handlebars, templateLoader);
		}

		private Collection<HandlebarsCustomizer> handlebarsCustomizers(ApplicationContext applicationContext) {
			return applicationContext.getBeansOfType(HandlebarsCustomizer.class).values();
		}

		/**
		 * Create handlebars instance.
		 *
		 * @param applicationContext Application context.
		 * @param environment Application context environment.
		 * @param customizers Customizers applied on created {@link Handlebars} instance.
		 * @return Handlebars instance.
		 * @throws Exception If an error occurred while creating {@link Handlebars} bean.
		 */
		private Handlebars handlebars(ApplicationContext applicationContext, Environment environment, Collection<HandlebarsCustomizer> customizers) throws Exception {
			try {
				return applicationContext.getBean(Handlebars.class);
			}
			catch (NoSuchBeanDefinitionException ex) {
				log.warn(ex.getMessage());

				HandlebarsConfiguration handlebarsConfiguration = new HandlebarsConfiguration(environment);
				handlebarsConfiguration.setCustomizers(customizers);

				HandlebarsFactoryBean factoryBean = handlebarsConfiguration.handlebarsCompiler();
				factoryBean.afterPropertiesSet();
				return factoryBean.getObject();
			}
		}
	},

	/**
	 * Mustache implementation that use mustache.java java as
	 * internal compiler.
	 */
	MUSTACHE_JAVA {
		@Override
		public boolean isAvailable() {
			return isPresent("com.github.mustachejava.MustacheFactory");
		}

		@Override
		public String configurationClass() {
			return MustacheJavaConfiguration.class.getName();
		}

		@Override
		MustacheCompiler doInstantiate(ApplicationContext applicationContext, Environment environment, MustacheTemplateLoader templateLoader) {
			MustacheFactory mustacheFactory = mustacheFactory(applicationContext, environment, templateLoader);
			return new MustacheJavaConfiguration(environment).mustacheCompiler(mustacheFactory, templateLoader);
		}

		private MustacheFactory mustacheFactory(ApplicationContext applicationContext, Environment environment, MustacheTemplateLoader templateLoader) {
			try {
				return applicationContext.getBean(MustacheFactory.class);
			}
			catch (NoSuchBeanDefinitionException ex) {
				MustacheJavaConfiguration mustacheJavaConfiguration = new MustacheJavaConfiguration(environment);
				MustacheResolver mustacheResolver = mustacheJavaConfiguration.mustacheResolver(templateLoader);
				return mustacheJavaConfiguration.mustacheFactory(mustacheResolver, templateLoader);
			}
		}
	},

	/**
	 * Option that detect class available on classpath
	 * and select the best implementation.
	 */
	AUTO {
		@Override
		public boolean isAvailable() {
			try {
				detectProvider();
				return true;
			}
			catch (IllegalArgumentException ex) {
				return false;
			}
		}

		@Override
		public String configurationClass() {
			return detectProvider().configurationClass();
		}

		@Override
		MustacheCompiler doInstantiate(ApplicationContext applicationContext, Environment environment, MustacheTemplateLoader templateLoader) throws Exception {
			return detectProvider().doInstantiate(applicationContext, environment, templateLoader);
		}
	};

	/**
	 * Get configuration class to import.
	 *
	 * @return Configuration class.
	 */
	public abstract String configurationClass();

	/**
	 * Instantiate compiler using appropriate implementation.
	 *
	 * @param applicationContext Application to retrieve dependent beans.
	 * @return Mustache compiler.
	 * @throws Exception If an error occurred while instantiating bean.
	 */
	public MustacheCompiler instantiate(ApplicationContext applicationContext) throws Exception {
		MustacheTemplateLoader templateLoader = mustacheTemplateLoader(applicationContext);
		Environment environment = applicationContext.getBean(Environment.class);
		return doInstantiate(applicationContext, environment, templateLoader);
	}

	/**
	 * Create mustache compiler to use application context.
	 *
	 * @param applicationContext The application context, used to retrieve dependencies.
	 * @param environment Application Context environment.
	 * @param templateLoader Template loader.
	 * @return The mustache compiler.
	 * @throws Exception If an error occurred while instantiating compiler.
	 */
	abstract MustacheCompiler doInstantiate(ApplicationContext applicationContext, Environment environment, MustacheTemplateLoader templateLoader) throws Exception;

	/**
	 * Get mustache template loader from given application context.
	 *
	 * @param applicationContext The application context.
	 * @return The template loader implementation.
	 */
	MustacheTemplateLoader mustacheTemplateLoader(ApplicationContext applicationContext) {
		return applicationContext.getBean(MustacheTemplateLoader.class);
	}

	/**
	 * Check if implementation is available.
	 *
	 * @return True if implementation can safely be used, false otherwise.
	 */
	public abstract boolean isAvailable();

	/**
	 * Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(MustacheProvider.class);

	/**
	 * Detect mustache provider.
	 * Use classpath detection under the hood.
	 *
	 * @return Available mustache provider.
	 */
	private static MustacheProvider detectProvider() {
		MustacheProvider[] values = MustacheProvider.values();

		// Sort by natural order (i.e priority).
		sort(values);

		for (MustacheProvider provider : values) {
			if (provider != AUTO && provider.isAvailable()) {
				log.debug("Provider '{}' available, use configuration", provider.name());
				return provider;
			}
			else {
				log.trace("Provider '{}' is missing, skip", provider.name());
			}
		}

		// No implementation detected, throw exception
		log.error("Mustache implementation is missing, please add one of following dependency to your classpath: {}", MustacheProvider.values());
		throw new IllegalArgumentException("Mustache implementation is missing, please add jmustache, handlebars, mustacheJava to classpath");
	}
}
