package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import java.io.Writer;
import java.util.Map;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.samskivert.mustache.Template;

/**
 * Implementation of {@link MustacheTemplate} using JMustache
 * as real template implementation.
 */
public class JMustacheTemplate implements MustacheTemplate {

	/**
	 * JMustache template.
	 * This template will be rendered using jmustache api.
	 */
	private final Template template;

	/**
	 * Build new template.
	 *
	 * @param template JMustache template.
	 */
	public JMustacheTemplate(Template template) {
		this.template = template;
	}

	@Override
	public void execute(Map<String, Object> model, Writer writer) {
		template.execute(model, writer);
	}
}
