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

package com.github.mjeanroy.springmvc.view.mustache.core;

import com.github.mjeanroy.springmvc.view.mustache.MustacheSettings;
import com.github.mjeanroy.springmvc.view.mustache.MustacheView;
import com.github.mjeanroy.springmvc.view.mustache.exceptions.MustachePartialsMappingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * Extension of {@link org.springframework.web.servlet.ModelAndView} class
 * that provide shortcuts to get and add partial mappings that will be used
 * to render view.
 */
public class ModelAndMustacheView extends ModelAndView {

	/**
	 * Default constructor for bean-style usage: populating bean
	 * properties instead of passing in constructor arguments.
	 * @see #setView(org.springframework.web.servlet.View)
	 * @see #setViewName(String)
	 */
	public ModelAndMustacheView() {
		super();
	}

	/**
	 * Convenient constructor when there is no model data to expose.
	 * Can also be used in conjunction with {@code addObject}.
	 * @param viewName name of the View to render, to be resolved
	 * by the DispatcherServlet's ViewResolver
	 * @see #addObject
	 */
	public ModelAndMustacheView(String viewName) {
		super(viewName);
	}

	/**
	 * Convenient constructor when there is no model data to expose.
	 * Can also be used in conjunction with {@code addObject}.
	 * @param view View object to render
	 * @see #addObject
	 */
	public ModelAndMustacheView(MustacheView view) {
		super(view);
	}

	/**
	 * Creates new ModelAndView given a view name and a model.
	 * @param viewName name of the View to render, to be resolved
	 * by the DispatcherServlet's ViewResolver
	 * @param model Map of model names (Strings) to model objects
	 * (Objects). Model entries may not be {@code null}, but the
	 * model Map may be {@code null} if there is no model data.
	 */
	public ModelAndMustacheView(String viewName, Map<String, ?> model) {
		super(viewName, model);
	}

	/**
	 * Creates new ModelAndView given a View object and a model.
	 * Note: the supplied model data is copied into the internal
	 * storage of this class. You should not consider to modify the supplied
	 * Map after supplying it to this class
	 * @param view View object to render
	 * @param model Map of model names (Strings) to model objects
	 * (Objects). Model entries may not be {@code null}, but the
	 * model Map may be {@code null} if there is no model data.
	 */
	public ModelAndMustacheView(MustacheView view, Map<String, ?> model) {
		super(view, model);
	}

	/**
	 * Convenient constructor to take a single model object.
	 * @param viewName name of the View to render, to be resolved
	 * by the DispatcherServlet's ViewResolver
	 * @param modelName name of the single entry in the model
	 * @param modelObject the single model object
	 */
	public ModelAndMustacheView(String viewName, String modelName, Object modelObject) {
		super(viewName, modelName, modelObject);
	}

	/**
	 * Convenient constructor to take a single model object.
	 * @param view View object to render
	 * @param modelName name of the single entry in the model
	 * @param modelObject the single model object
	 */
	public ModelAndMustacheView(MustacheView view, String modelName, Object modelObject) {
		super(view, modelName, modelObject);
	}

	/**
	 * Get current partials stored in view.
	 *
	 * @return Current partials.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPartials() {
		final View view = this.getView();
		if (view != null && view instanceof MustacheView) {
			MustacheView mustacheView = (MustacheView) view;
			return mustacheView.getAliases();
		}
		else {
			final Object object = getModelMap().get(MustacheSettings.PARTIALS_KEY);

			if (object != null && !(object instanceof Map)) {
				throw new MustachePartialsMappingException();
			}

			final Map<String, String> map;

			if (object == null) {
				map = new HashMap<String, String>();
			}
			else {
				map = (Map<String, String>) object;
			}

			return map;
		}
	}

	/**
	 * Add new partials mapping to the view.
	 *
	 * @param key Partial name.
	 * @param name Partial path.
	 */
	public void addPartial(final String key, final String name) {
		notNull(key);
		notNull(name);

		View view = this.getView();
		if (view != null && view instanceof MustacheView) {
			// First try to not pollute view model object
			final MustacheView mustacheView = (MustacheView) view;
			mustacheView.addAlias(key, name);
		}
		else {
			// Otherwise, add partials to model object
			final Map<String, String> currentPartials = getPartials();
			currentPartials.put(key, name);
			super.addObject(MustacheSettings.PARTIALS_KEY, currentPartials);
		}
	}

	/**
	 * Add new partials mapping to the view.
	 *
	 * @param partials Partials.
	 */
	public void addPartials(final Map<String, String> partials) {
		notNull(partials);

		View view = this.getView();
		if (view != null && view instanceof MustacheView) {
			// First try to not pollute model object
			final MustacheView mustacheView = (MustacheView) view;
			mustacheView.addAliases(partials);
		}
		else {
			// Otherwise, add partials to model object
			final Map<String, String> currentPartials = getPartials();
			for (Map.Entry<String, String> entry : partials.entrySet()) {
				notNull(entry.getKey());
				notNull(entry.getValue());
				currentPartials.put(entry.getKey(), entry.getValue());
			}
			super.addObject(MustacheSettings.PARTIALS_KEY, currentPartials);
		}
	}
}
