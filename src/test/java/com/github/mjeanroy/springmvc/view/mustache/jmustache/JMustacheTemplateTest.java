package com.github.mjeanroy.springmvc.view.mustache.jmustache;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.samskivert.mustache.Template;

public class JMustacheTemplateTest {

	private Template template;

	private JMustacheTemplate jMustacheTemplate;

	@Before
	public void setUp() {
		template = mock(Template.class);
		jMustacheTemplate = new JMustacheTemplate(template);
	}

	@Test
	public void it_should_execute_template() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("foo", "bar");

		Writer writer = mock(Writer.class);

		jMustacheTemplate.execute(model, writer);

		verify(template).execute(model, writer);
	}
}
