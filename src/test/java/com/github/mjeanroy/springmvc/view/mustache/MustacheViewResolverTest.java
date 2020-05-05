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

package com.github.mjeanroy.springmvc.view.mustache;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MustacheViewResolverTest {

	private MustacheCompiler compiler;

	private MustacheViewResolver mustacheViewResolver;

	@Before
	public void setUp() {
		compiler = mock(MustacheCompiler.class);
		mustacheViewResolver = new MustacheViewResolver(compiler);
	}

	@Test
	public void it_should_have_required_view_class() {
		assertThat(mustacheViewResolver.requiredViewClass()).isNotNull().isEqualTo(MustacheView.class);
	}

	@Test
	public void it_should_build_resolver_using_compiler_and_template_loader() {
		MustacheCompiler compiler = mock(MustacheCompiler.class);

		MustacheViewResolver mustacheViewResolver = new MustacheViewResolver(compiler);

		MustacheCompiler c = readField(mustacheViewResolver, "compiler");
		String prefix = readField(mustacheViewResolver, "prefix");
		String suffix = readField(mustacheViewResolver, "suffix");
		String defaultLayout = readField(mustacheViewResolver, "defaultLayout");
		String layoutKey = readField(mustacheViewResolver, "layoutKey");
		Map<String, String> mappings = readField(mustacheViewResolver, "layoutMappings");

		assertThat(c).isNotNull().isSameAs(compiler);
		assertThat(prefix).isNotNull().isEmpty();
		assertThat(suffix).isNotNull().isEmpty();
		assertThat(defaultLayout).isNull();
		assertThat(layoutKey).isNotNull().isEqualTo(MustacheSettings.DEFAULT_LAYOUT_KEY);
		assertThat(mappings).isNotNull().isEmpty();
	}

	@Test
	public void it_should_set_prefix() {
		String prefix = "foo";

		mustacheViewResolver.setPrefix(prefix);

		String p = readField(mustacheViewResolver, "prefix");
		assertThat(p).isNotNull().isEqualTo(prefix);

		verify(compiler).setPrefix(prefix);
	}

	@Test
	public void it_should_set_suffix() {
		String suffix = "foo";

		mustacheViewResolver.setSuffix(suffix);

		String p = readField(mustacheViewResolver, "suffix");
		assertThat(p).isNotNull().isEqualTo(suffix);

		verify(compiler).setSuffix(suffix);
	}

	@Test
	public void it_should_default_layout() {
		String defaultLayout = "foo";

		mustacheViewResolver.setDefaultLayout(defaultLayout);

		String dl = readField(mustacheViewResolver, "defaultLayout");
		assertThat(dl).isNotNull().isEqualTo(defaultLayout);
	}

	@Test
	public void it_should_layout_key() {
		String layoutKey = "foo";

		mustacheViewResolver.setLayoutKey(layoutKey);

		String lk = readField(mustacheViewResolver, "layoutKey");
		assertThat(lk).isNotNull().isEqualTo(layoutKey);
	}

	@Test
	public void it_should_add_layout_mappings() {
		String viewName = "foo";
		String layoutName = "bar";

		Map<String, String> mappings = readField(mustacheViewResolver, "layoutMappings");
		assertThat(mappings).isNotNull().isEmpty();

		mustacheViewResolver.addLayoutMapping(viewName, layoutName);

		mappings = readField(mustacheViewResolver, "layoutMappings");
		assertThat(mappings).isNotNull().hasSize(1).contains(
				entry(viewName, layoutName)
		);
	}

	@Test
	public void it_should_replace_layout_mappings() {
		String viewName = "foo";
		String layoutName = "bar";

		mustacheViewResolver.addLayoutMapping(viewName, layoutName);

		Map<String, String> mappings = readField(mustacheViewResolver, "layoutMappings");
		assertThat(mappings).isNotNull().hasSize(1).contains(
				entry(viewName, layoutName)
		);

		String newViewName = "foo1";
		String newLayoutName = "bar1";
		Map<String, String> newMappings = new HashMap<String, String>();
		newMappings.put(newViewName, newLayoutName);

		mustacheViewResolver.setLayoutMappings(newMappings);

		mappings = readField(mustacheViewResolver, "layoutMappings");
		assertThat(mappings).isNotNull().hasSize(1).contains(
				entry(newViewName, newLayoutName)
		);
	}

	@Test
	public void it_should_build_view() throws Exception {
		String viewName = "foo";

		MustacheView mustacheView = mustacheViewResolver.buildView(viewName);

		assertThat(mustacheView).isNotNull();
		assertThat(mustacheView.getCompiler()).isNotNull().isSameAs(compiler);
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
		assertThat(mustacheView.getUrl()).isNotNull().isEqualTo(layout);

		// Check partials mapping
		Map<String, String> partialsAliases = readField(mustacheView, "aliases");
		assertThat(partialsAliases).isNotNull().isNotEmpty().hasSize(1).contains(
				entry(MustacheSettings.DEFAULT_LAYOUT_KEY, viewName)
		);
	}

	@Test
	public void it_should_build_view_using_layout_mappings() throws Exception {
		String layout1 = "index";
		String layout2 = "admin";

		mustacheViewResolver.addLayoutMapping("bar", layout2);
		mustacheViewResolver.setDefaultLayout(layout1);

		String viewName1 = "foo";
		String viewName2 = "bar";

		MustacheView mustacheView1 = mustacheViewResolver.buildView(viewName1);
		MustacheView mustacheView2 = mustacheViewResolver.buildView(viewName2);

		// Check first view
		assertThat(mustacheView1).isNotNull();
		assertThat(mustacheView1.getCompiler()).isNotNull().isSameAs(compiler);
		assertThat(mustacheView1.getUrl()).isNotNull().isEqualTo(layout1);
		Map<String, String> partialsAliases1 = readField(mustacheView1, "aliases");
		assertThat(partialsAliases1).isNotNull().isNotEmpty().hasSize(1).contains(
				entry(MustacheSettings.DEFAULT_LAYOUT_KEY, viewName1)
		);

		// Check second view
		assertThat(mustacheView2).isNotNull();
		assertThat(mustacheView2.getCompiler()).isNotNull().isSameAs(compiler);
		assertThat(mustacheView2.getUrl()).isNotNull().isEqualTo(layout2);
		Map<String, String> partialsAliases2 = readField(mustacheView2, "aliases");
		assertThat(partialsAliases2).isNotNull().isNotEmpty().hasSize(1).contains(
				entry(MustacheSettings.DEFAULT_LAYOUT_KEY, viewName2)
		);
	}
}
