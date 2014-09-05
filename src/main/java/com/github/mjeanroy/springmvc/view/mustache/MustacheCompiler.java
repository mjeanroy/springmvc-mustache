package com.github.mjeanroy.springmvc.view.mustache;

import java.util.Map;

/**
 * Mustache compiler that can be used to compile mustache
 * templates with optional partials.
 */
public interface MustacheCompiler {

	/**
	 * Compile template.
	 * This compilation allow partial aliases to be used for template compilation.
	 *
	 * @param name Template name.
	 * @param partialAliases Partials aliases that will be used during template compilation.
	 * @return Compiled template.
	 */
	MustacheTemplate compile(String name, Map<String, String> partialAliases);

	/**
	 * Compile template.
	 *
	 * @param name Template name.
	 * @return Compiled template.
	 */
	MustacheTemplate compile(String name);

	/**
	 * Set prefix to prepend to template names before it is compiled.
	 *
	 * @param prefix New prefix value.
	 */
	void setPrefix(String prefix);

	/**
	 * Set suffix to append to template names before it is compiled.
	 *
	 * @param suffix New suffix value.
	 */
	void setSuffix(String suffix);
}
