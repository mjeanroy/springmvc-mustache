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

package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import static com.samskivert.mustache.Mustache.TemplateLoader;
import static org.springframework.util.Assert.notNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheTemplateNotFoundException;

/**
 * Mustache template loader.
 *
 * Resource are retrieved using {@link DefaultResourceLoader} by default unless a
 * specific resource loader is used during construction.
 *
 * Prefix and Suffix can be set, these will be used to retrieve template by its name (if given
 * does not already starts with prefix and does not already ends with suffix).
 *
 * For example:
 * - If prefix and suffix are null:
 *   getTemplate("foo"); // Call internally resourceLoader.getResource("foo");
 *
 * - If prefix or suffix are not null:
 *   getTemplate("foo"); // Call internally resourceLoader.getResource({prefix} + "foo" + {suffix});
 *
 * This class can be considered as thread safe if internal state is not modified (if prefix and suffix are not modified, or if aliases are not added).
 */
public class JMustacheTemplateLoader implements TemplateLoader, Cloneable {

	private static final Logger log = LoggerFactory.getLogger(JMustacheTemplateLoader.class);

	/**
	 * Resource loader that will be used to retrieve mustache template
	 * from template name.
	 */
	private final ResourceLoader resourceLoader;

	/**
	 * Prefix to prepend to resource before retrieving template name.
	 */
	// Volatile because it can be accessed by more than one thread
	private volatile String prefix;

	/**
	 * Suffix to append to resource before retrieving template name.
	 */
	// Volatile because it can be accessed by more than one thread
	private volatile String suffix;

	/**
	 * Partial aliases.
	 */
	private final Map<String, String> partialAliases;

	/**
	 * Build new template loader.
	 * Use {@link DefaultResourceLoader} as resource loader implementation.
	 */
	public JMustacheTemplateLoader() {
		this.partialAliases = defaultPartialAliases();
		this.resourceLoader = new DefaultResourceLoader();
		this.prefix = null;
		this.suffix = null;
	}

	/**
	 * Build new template loader.
	 *
	 * @param resourceLoader Resource loader implementation to use.
	 */
	public JMustacheTemplateLoader(ResourceLoader resourceLoader) {
		notNull(resourceLoader);

		this.partialAliases = defaultPartialAliases();
		this.resourceLoader = resourceLoader;
		this.prefix = null;
		this.suffix = null;
	}

	/**
	 * Build new template loader.
	 *
	 * @param resourceLoader Resource loader implementation to use.
	 * @param prefix Prefix to prepend to template name before loading it using resource loader.
	 * @param suffix Suffix to append to template before loading it using resource loader.
	 */
	public JMustacheTemplateLoader(ResourceLoader resourceLoader, String prefix, String suffix) {
		notNull(resourceLoader);
		notNull(prefix);
		notNull(suffix);

		this.partialAliases = defaultPartialAliases();
		this.resourceLoader = resourceLoader;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	private Map<String, String> defaultPartialAliases() {
		return new HashMap<String, String>();
	}

	/**
	 * Set prefix on template names.
	 *
	 * @param prefix New prefix.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Set suffix on template names.
	 *
	 * @param suffix New suffix.
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Add partials mapping.
	 *
	 * @param partialAliases Partials aliases.
	 */
	public void addPartialAliases(Map<String, String> partialAliases) {
		notNull(partialAliases);
		this.partialAliases.putAll(partialAliases);
	}

	@Override
	public Reader getTemplate(String name) throws Exception {
		return getTemplate(name, partialAliases);
	}

	public Reader getTemplate(String name, Map<String, String> partialsAliases) throws Exception {
		log.debug("Get template {}", name);

		String realName = name;
		if (partialsAliases.containsKey(name)) {
			realName = partialsAliases.get(name);
		}

		String templateName = formatName(realName);
		Resource resource = resourceLoader.getResource(templateName);

		if (!resource.exists()) {
			log.error("Template {} cannot be found using {}", templateName, resourceLoader.getClass().getName());
			throw new MustacheTemplateNotFoundException(templateName);
		}

		InputStream inputStream = resource.getInputStream();
		return new InputStreamReader(inputStream);
	}

	private String formatName(String name) {
		if (prefix == null && suffix == null) {
			log.debug("Prefix and suffix are null, do not format name");
			return name;
		}

		log.debug("Format template name using prefix '{}' and suffix '{}'", prefix, suffix);

		String result = name;

		if (prefix != null && !name.startsWith(prefix)) {
			result = prefix + result;
		}

		if (suffix != null && !name.endsWith(suffix)) {
			result = result + suffix;
		}

		return result;
	}

	@Override
	public JMustacheTemplateLoader clone() {
		JMustacheTemplateLoader templateLoader = new JMustacheTemplateLoader(resourceLoader);
		if (prefix != null) {
			templateLoader.setPrefix(prefix);
		}
		if (suffix != null) {
			templateLoader.setSuffix(suffix);
		}
		templateLoader.addPartialAliases(partialAliases);
		return templateLoader;
	}
}
