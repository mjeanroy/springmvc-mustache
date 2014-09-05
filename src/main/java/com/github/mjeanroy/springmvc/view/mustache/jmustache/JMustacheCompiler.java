package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import static com.samskivert.mustache.Mustache.Compiler;
import static java.util.Collections.emptyMap;

import java.io.Reader;
import java.util.Map;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplate;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustacheCompilationException;
import com.samskivert.mustache.Template;

/**
 * Mustache Compiler using JMustache as real implementation.
 */
public class JMustacheCompiler implements MustacheCompiler {

	/**
	 * Original JMustache compiler.
	 */
	private final Compiler compiler;

	/**
	 * Original JMustache template loader.
	 */
	private final JMustacheTemplateLoader templateLoader;

	/**
	 * Build new mustache compiler using JMustache API.
	 * This compiler need a {@link Compiler} to produce compiled template
	 * and a {@link JMustacheTemplateLoader} to load partials defined in templates.
	 *
	 * @param compiler JMustache Compiler.
	 * @param templateLoader Template Loader.
	 */
	public JMustacheCompiler(Compiler compiler, JMustacheTemplateLoader templateLoader) {
		this.compiler = compiler;
		this.templateLoader = templateLoader;
	}

	@Override
	public void setPrefix(String prefix) {
		templateLoader.setPrefix(prefix);
	}

	@Override
	public void setSuffix(String suffix) {
		templateLoader.setSuffix(suffix);
	}

	@Override
	public MustacheTemplate compile(String name) {
		Map<String, String> aliases = emptyMap();
		return compile(name, aliases);
	}

	@Override
	public MustacheTemplate compile(String name, Map<String, String> aliases) {
		final JMustacheTemplateLoader templateLoader;

		// If aliases is not empty, we should use a template loader that define
		// these aliases only for this compilation.
		if (!aliases.isEmpty()) {
			templateLoader = this.templateLoader.clone();
			templateLoader.addPartialAliases(aliases);
		} else {
			templateLoader = this.templateLoader;
		}

		try {
			final Reader template = templateLoader.getTemplate(name);
			final Template result = getTemplate(template, templateLoader);
			return new JMustacheTemplate(result);
		}
		catch (Exception ex) {
			throw new MustacheCompilationException(ex);
		}
	}

	private Template getTemplate(Reader template, JMustacheTemplateLoader templateLoader) {
		return compiler
				.withLoader(templateLoader)
				.compile(template);
	}
}
