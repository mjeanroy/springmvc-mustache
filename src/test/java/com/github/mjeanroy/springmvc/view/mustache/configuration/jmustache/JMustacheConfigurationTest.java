package com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache;

import com.github.mjeanroy.springmvc.view.mustache.MustacheCompiler;
import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.Environment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JMustacheConfigurationTest {

	@Mock
	private Environment environment;

	@InjectMocks
	private JMustacheConfiguration jMustacheConfiguration;

	@Before
	public void setUp() {
		when(environment.getProperty(anyString(), anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[1];
			}
		});
	}

	@Test
	public void it_should_instantiate_template_loader() {
		MustacheTemplateLoader templateLoader = jMustacheConfiguration.mustacheTemplateLoader();
		assertThat(templateLoader).isNotNull();
	}

	@Test
	public void it_should_instantiate_mustache_compiler() {
		MustacheCompiler mustacheCompiler = jMustacheConfiguration.mustacheCompiler();
		assertThat(mustacheCompiler).isNotNull();
	}

	@Test
	public void it_should_instantiate_mustache_view_resolver() {
		MustacheViewResolver mustacheViewResolver = jMustacheConfiguration.mustacheViewResolver();
		assertThat(mustacheViewResolver).isNotNull();
	}

	@Test
	public void it_should_parse_layout_mappings() {
		String admin1 = "admin1";
		String admin2 = "admin2";
		String secure = "secure";

		String mappings = admin1 + ":" + secure + ";" + admin2 + ":" + secure;
		when(environment.getProperty("mustache.layoutMappings", "")).thenReturn(mappings);

		Map<String, String> map = jMustacheConfiguration.getLayoutMappings();

		assertThat(map).isNotNull().isNotEmpty().hasSize(2)
				.containsOnly(
						entry(admin1, secure),
						entry(admin2, secure)
				);
	}

	@Test
	public void it_should_parse_view_names() {
		String n1 = "*.template.html";
		String n2 = "*.mustache";
		String viewNames = n1 + ", " + n2;
		when(environment.getProperty("mustache.viewNames", "*")).thenReturn(viewNames);

		String[] names = jMustacheConfiguration.getViewNames();

		assertThat(names).isNotNull().isNotEmpty().hasSize(2).containsOnly(n1, n2);
	}
}
