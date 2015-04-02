/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 <mickael.jeanroy@gmail.com>
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

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import org.springframework.context.MessageSource;

import com.github.mjeanroy.springmvc.view.mustache.i18n.MustacheI18nLambdaFactory;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

public class JMustacheI18Factory implements MustacheI18nLambdaFactory {

	@Override
	public Object createLambda(Locale locale, MessageSource messageSource) {
		return new I18nLambda(locale, messageSource);
	}

	private static final class I18nLambda implements Mustache.Lambda {

		private final Locale locale;

		private final MessageSource messageSource;

		public I18nLambda(Locale locale, MessageSource messageSource) {
			this.locale = locale;
			this.messageSource = messageSource;
		}

		@Override
		public void execute(Template.Fragment frag, Writer out) throws IOException {
			String key = frag.execute();
			String message = messageSource.getMessage(key, null, locale);
			out.write(message);
		}
	}
}
