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

import com.github.mjeanroy.springmvc.view.mustache.core.DefaultTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.jmustache.JMustacheCompiler;
import com.samskivert.mustache.Mustache;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.hexIdentity;
import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class MustacheViewResolverTest {

	@Test
	public void it_should_create_view_resolver() {
		MustacheCompiler mustacheCompiler = mustacheCompiler();
		MustacheViewResolver viewResolver = mustacheViewResolver(mustacheCompiler);

		assertThat(viewResolver.getOrder()).isEqualTo(2147483647);
		assertThat(viewResolver.isCache()).isTrue();
		assertThat(viewResolver.getCacheLimit()).isEqualTo(1024);
	}

	@Test
	public void it_should_have_required_view_class() {
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver();
		Class<?> viewClass = mustacheViewResolver.requiredViewClass();
		assertThat(viewClass).isNotNull().isEqualTo(MustacheView.class);
	}

	@Test
	public void it_should_build_resolver_using_compiler_and_template_loader() {
		MustacheCompiler compiler = mustacheCompiler();
		MustacheViewResolver mustacheViewResolver = new MustacheViewResolver(compiler);

		MustacheCompiler mustacheCompiler = readField(mustacheViewResolver, "compiler");
		String prefix = readField(mustacheViewResolver, "prefix");
		String suffix = readField(mustacheViewResolver, "suffix");
		String defaultLayout = readField(mustacheViewResolver, "defaultLayout");
		String layoutKey = readField(mustacheViewResolver, "layoutKey");
		Map<String, String> mappings = readField(mustacheViewResolver, "layoutMappings");

		assertThat(mustacheCompiler).isNotNull().isSameAs(compiler);
		assertThat(prefix).isNotNull().isEmpty();
		assertThat(suffix).isNotNull().isEmpty();
		assertThat(defaultLayout).isNull();
		assertThat(layoutKey).isNotNull().isEqualTo(MustacheSettings.DEFAULT_LAYOUT_KEY);
		assertThat(mappings).isNotNull().isEmpty();
	}

	@Test
	public void it_should_set_prefix() {
		String prefix = "foo";
		MustacheCompiler mustacheCompiler = mustacheCompiler();
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver(mustacheCompiler);

		mustacheViewResolver.setPrefix(prefix);

		assertThat(readField(mustacheViewResolver, "prefix", String.class)).isEqualTo(prefix);
		assertThat(mustacheCompiler.getPrefix()).isEqualTo(prefix);
	}

	@Test
	public void it_should_set_suffix() {
		String suffix = "foo";
		MustacheCompiler mustacheCompiler = mustacheCompiler();
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver(mustacheCompiler);

		mustacheViewResolver.setSuffix(suffix);

		assertThat(readField(mustacheViewResolver, "suffix", String.class)).isEqualTo(suffix);
		assertThat(mustacheCompiler.getSuffix()).isEqualTo(suffix);
	}

	@Test
	public void it_should_set_default_layout() {
		String defaultLayout = "foo";
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver();
		mustacheViewResolver.setDefaultLayout(defaultLayout);
		assertThat(readField(mustacheViewResolver, "defaultLayout", String.class)).isEqualTo(defaultLayout);
	}

	@Test
	public void it_should_set_layout_key() {
		String layoutKey = "foo";
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver();
		mustacheViewResolver.setLayoutKey(layoutKey);
		assertThat(readField(mustacheViewResolver, "layoutKey", String.class)).isEqualTo(layoutKey);
	}

	@Test
	public void it_should_add_layout_mappings() {
		String viewName = "foo";
		String layoutName = "bar";

		MustacheViewResolver mustacheViewResolver = mustacheViewResolver();
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
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver();

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
		MustacheCompiler mustacheCompiler = mustacheCompiler();
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver(mustacheCompiler);

		MustacheView mustacheView = mustacheViewResolver.buildView(viewName);

		assertThat(mustacheView).isNotNull();
		assertThat(mustacheView.getCompiler()).isSameAs(mustacheCompiler);
		assertThat(mustacheView.getAliases()).isEmpty();
		assertThat(mustacheView.getUrl()).isEqualTo(viewName);

		assertThat(mustacheView.getContentType()).isEqualTo("text/html; charset=utf-8");
		assertThat(mustacheView.isExposePathVariables()).isTrue();
	}

	@Test
	public void it_should_build_view_using_layout() throws Exception {
		String layout = "index";
		MustacheCompiler mustacheCompiler = mustacheCompiler();
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver(mustacheCompiler);
		mustacheViewResolver.setDefaultLayout(layout);

		String viewName = "foo";

		MustacheView mustacheView = mustacheViewResolver.buildView(viewName);

		assertThat(mustacheView).isNotNull();
		assertThat(mustacheView.getCompiler()).isSameAs(mustacheCompiler);
		assertThat(mustacheView.getUrl()).isEqualTo(layout);

		// Check partials mapping
		Map<String, String> partialsAliases = readField(mustacheView, "aliases");
		assertThat(partialsAliases).hasSize(1).contains(
				entry(MustacheSettings.DEFAULT_LAYOUT_KEY, viewName)
		);
	}

	@Test
	public void it_should_build_view_using_layout_mappings() throws Exception {
		String layout1 = "index";
		String layout2 = "admin";

		MustacheCompiler mustacheCompiler = mustacheCompiler();
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver(mustacheCompiler);
		mustacheViewResolver.addLayoutMapping("bar", layout2);
		mustacheViewResolver.setDefaultLayout(layout1);

		String viewName1 = "foo";
		String viewName2 = "bar";

		MustacheView mustacheView1 = mustacheViewResolver.buildView(viewName1);
		MustacheView mustacheView2 = mustacheViewResolver.buildView(viewName2);

		// Check first view
		assertThat(mustacheView1).isNotNull();
		assertThat(mustacheView1.getCompiler()).isSameAs(mustacheCompiler);
		assertThat(mustacheView1.getUrl()).isEqualTo(layout1);
		Map<String, String> partialsAliases1 = readField(mustacheView1, "aliases");
		assertThat(partialsAliases1).hasSize(1).contains(
				entry(MustacheSettings.DEFAULT_LAYOUT_KEY, viewName1)
		);

		// Check second view
		assertThat(mustacheView2).isNotNull();
		assertThat(mustacheView2.getCompiler()).isSameAs(mustacheCompiler);
		assertThat(mustacheView2.getUrl()).isEqualTo(layout2);
		Map<String, String> partialsAliases2 = readField(mustacheView2, "aliases");
		assertThat(partialsAliases2).hasSize(1).contains(
				entry(MustacheSettings.DEFAULT_LAYOUT_KEY, viewName2)
		);
	}

	@Test
	public void it_should_implement_to_string() {
		String layout1 = "index";
		String layout2 = "admin";

		MustacheCompiler mustacheCompiler = mustacheCompiler();
		MustacheViewResolver mustacheViewResolver = mustacheViewResolver(mustacheCompiler);
		mustacheViewResolver.addLayoutMapping("bar", layout2);
		mustacheViewResolver.setDefaultLayout(layout1);

		// @formatter:off
		String expectedToString =
				"com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver@%s{" +
						"compiler=%s, " +
						"defaultLayout=\"index\", " +
						"layoutKey=\"content\", " +
						"layoutMappings={bar=admin}, " +
						"order=2147483647, " +
						"prefix=\"\", " +
						"suffix=\"\", " +
						"contentType=null, " +
						"exposeContextBeansAsAttributes=null, " +
						"exposedContextBeanNames=null, " +
						"exposePathVariables=null, " +
						"cache=true, " +
						"cacheLimit=1024" +
				"}";
		// @formatter:on

		assertThat(mustacheViewResolver).hasToString(String.format(
				expectedToString, hexIdentity(mustacheViewResolver), mustacheCompiler
		));
	}

	private static MustacheCompiler mustacheCompiler() {
		Mustache.Compiler jMustacheCompiler = Mustache.compiler();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		MustacheTemplateLoader templateLoader = new DefaultTemplateLoader(resourceLoader);
		return new JMustacheCompiler(jMustacheCompiler, templateLoader);
	}

	private static MustacheViewResolver mustacheViewResolver() {
		return new MustacheViewResolver(mustacheCompiler());
	}

	private static MustacheViewResolver mustacheViewResolver(MustacheCompiler mustacheCompiler) {
		return new MustacheViewResolver(mustacheCompiler);
	}
}
