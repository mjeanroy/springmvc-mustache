package com.github.mjeanroy.springmvc.view.mustache;

import java.io.Writer;
import java.util.Map;

/**
 * Mustache template that is already compiled and can be rendered using given model
 * data and writer output.
 */
public interface MustacheTemplate {

	/**
	 * Render template and print output to given writer.
	 *
	 * @param model Model object that will be rendered into template.
	 * @param writer Writer output.
	 */
	void execute(Map<String, Object> model, Writer writer);
}
