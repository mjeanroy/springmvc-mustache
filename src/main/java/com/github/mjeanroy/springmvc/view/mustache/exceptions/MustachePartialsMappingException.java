package com.github.mjeanroy.springmvc.view.mustache.exceptions;

import com.github.mjeanroy.springmvc.view.mustache.MustacheSettings;

import static java.lang.String.format;

/**
 * Exception thrown when partials mapping cannot be retrieved
 * because it is not a valid object.
 */
public final class MustachePartialsMappingException extends RuntimeException {

	/**
	 * Build exception with default message.
	 */
	public MustachePartialsMappingException() {
		super(format(
				"Partial object stored in '%s' is not a valid map object",
				MustacheSettings.PARTIALS_KEY
		));
	}
}
