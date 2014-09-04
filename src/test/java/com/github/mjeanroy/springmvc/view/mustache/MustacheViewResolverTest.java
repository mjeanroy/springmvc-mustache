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

package com.github.mjeanroy.springmvc.view.mustache;

import static com.samskivert.mustache.Mustache.Compiler;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheTemplateLoader;

@SuppressWarnings("unchecked")
public class MustacheViewResolverTest {

	private Compiler compiler;

	private JMustacheTemplateLoader templateLoader;

	private MustacheViewResolver mustacheViewResolver;

	@Before
	public void setUp() {
		compiler = mock(Compiler.class);
		templateLoader = mock(JMustacheTemplateLoader.class);
		mustacheViewResolver = new MustacheViewResolver(compiler, templateLoader);
	}

	@Test
	public void it_should_have_required_view_class() {
		assertThat(mustacheViewResolver.requiredViewClass()).isNotNull().isEqualTo(MustacheView.class);
	}

	@Test
	public void it_should_build_resolver_using_compiler_and_template_loader() throws Exception {
		Compiler compiler = mock(Compiler.class);
		JMustacheTemplateLoader templateLoader = mock(JMustacheTemplateLoader.class);

		MustacheViewResolver mustacheViewResolver = new MustacheViewResolver(compiler, templateLoader);

		Compiler c = (Compiler) readField(mustacheViewResolver, "compiler", true);
		JMustacheTemplateLoader t = (JMustacheTemplateLoader) readField(mustacheViewResolver, "templateLoader", true);
		String prefix = (String) readField(mustacheViewResolver, "prefix", true);
		String suffix = (String) readField(mustacheViewResolver, "suffix", true);
		String defaultLayout = (String) readField(mustacheViewResolver, "defaultLayout", true);
		String layoutKey = (String) readField(mustacheViewResolver, "layoutKey", true);

		assertThat(c).isNotNull().isSameAs(compiler);
		assertThat(t).isNotNull().isSameAs(templateLoader);
		assertThat(prefix).isNotNull().isEmpty();
		assertThat(suffix).isNotNull().isEmpty();
		assertThat(defaultLayout).isNull();
		assertThat(layoutKey).isNotNull().isEqualTo(MustacheViewResolver.DEFAULT_LAYOUT_KEY);
	}

	@Test
	public void it_should_build_resolver_using_compiler_and_resource_loader() throws Exception {
		Compiler compiler = mock(Compiler.class);
		ResourceLoader resourceLoader = mock(ResourceLoader.class);

		MustacheViewResolver mustacheViewResolver = new MustacheViewResolver(compiler, resourceLoader);

		Compiler c = (Compiler) readField(mustacheViewResolver, "compiler", true);
		JMustacheTemplateLoader t = (JMustacheTemplateLoader) readField(mustacheViewResolver, "templateLoader", true);
		String prefix = (String) readField(mustacheViewResolver, "prefix", true);
		String suffix = (String) readField(mustacheViewResolver, "suffix", true);
		String defaultLayout = (String) readField(mustacheViewResolver, "defaultLayout", true);
		String layoutKey = (String) readField(mustacheViewResolver, "layoutKey", true);

		assertThat(c).isNotNull().isSameAs(compiler);
		assertThat(t).isNotNull();
		assertThat(prefix).isNotNull().isEmpty();
		assertThat(suffix).isNotNull().isEmpty();
		assertThat(defaultLayout).isNull();
		assertThat(layoutKey).isNotNull().isEqualTo(MustacheViewResolver.DEFAULT_LAYOUT_KEY);

		ResourceLoader r = (ResourceLoader) readField(t, "resourceLoader", true);
		assertThat(r).isNotNull().isSameAs(resourceLoader);
	}

	@Test
	public void it_should_set_prefix() throws Exception {
		String prefix = "foo";

		mustacheViewResolver.setPrefix(prefix);

		String p = (String) readField(mustacheViewResolver, "prefix", true);
		assertThat(p).isNotNull().isEqualTo(prefix);

		verify(templateLoader).setPrefix(prefix);
	}

	@Test
	public void it_should_set_suffix() throws Exception {
		String suffix = "foo";

		mustacheViewResolver.setSuffix(suffix);

		String p = (String) readField(mustacheViewResolver, "suffix", true);
		assertThat(p).isNotNull().isEqualTo(suffix);

		verify(templateLoader).setSuffix(suffix);
	}

	@Test
	public void it_should_default_layout() throws Exception {
		String defaultLayout = "foo";

		mustacheViewResolver.setDefaultLayout(defaultLayout);

		String dl = (String) readField(mustacheViewResolver, "defaultLayout", true);
		assertThat(dl).isNotNull().isEqualTo(defaultLayout);
	}

	@Test
	public void it_should_layout_key() throws Exception {
		String layoutKey = "foo";

		mustacheViewResolver.setLayoutKey(layoutKey);

		String lk = (String) readField(mustacheViewResolver, "layoutKey", true);
		assertThat(lk).isNotNull().isEqualTo(layoutKey);
	}

	@Test
	public void it_should_build_view() throws Exception {
		String viewName = "foo";

		MustacheView mustacheView = mustacheViewResolver.buildView(viewName);

		assertThat(mustacheView).isNotNull();
		assertThat(mustacheView.getCompiler()).isNotNull().isSameAs(compiler);
		assertThat(mustacheView.getTemplateLoader()).isNotNull().isSameAs(templateLoader);
		assertThat(mustacheView.getUrl()).isNotNull().isEqualTo(viewName);
	}

	@Test
	public void it_should_build_view_using_layout() throws Exception {
		String layout = "index";
		mustacheViewResolver.setDefaultLayout(layout);

		String viewName = "foo";

		MustacheView mustacheView = mustacheViewResolver.buildView(viewName);

		assertThat(mustacheView).isNotNull();
		assertThat(mustacheView.getCompiler()).isNotNull().isSameAs(compiler);
		assertThat(mustacheView.getTemplateLoader()).isNotNull().isSameAs(templateLoader);
		assertThat(mustacheView.getUrl()).isNotNull().isEqualTo(layout);

		// Check partials mapping
		Map<String, String> partialsAliases = (Map<String, String>) readField(mustacheView, "aliases", true);
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(1).contains(
				entry(MustacheViewResolver.DEFAULT_LAYOUT_KEY, viewName)
		);
	}
}
