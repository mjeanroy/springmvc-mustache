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

package com.github.mjeanroy.springmvc.view.mustache.nashorn;

import com.github.mjeanroy.springmvc.view.mustache.MustacheTemplateLoader;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("deprecation")
public class NashornPartialsObjectTest {

	private MustacheTemplateLoader templateLoader;

	private NashornPartialsObject partials;

	@Before
	public void setUp() {
		templateLoader = mock(MustacheTemplateLoader.class);
		partials = new NashornPartialsObject(templateLoader);
	}

	@Test
	public void it_should_load_template() {
		String templateValue = "Hello World";
		String templateName = "foo";

		Reader reader = new StringReader(templateValue);
		when(templateLoader.getTemplate(templateName)).thenReturn(reader);

		String template = (String) partials.getMember(templateName);

		verify(templateLoader).getTemplate(templateName);
		assertThat(template)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo(templateValue);
	}

	@Test
	public void it_should_check_if_member_exist() {
		boolean hasMember = partials.hasMember("foo");
		assertThat(hasMember).isTrue();
		verifyNoMoreInteractions(templateLoader);
	}

	@Test
	public void it_should_fail_to_add_member() {
		ThrowingCallable setMember = new ThrowingCallable() {
			@Override
			public void call() {
				partials.setMember("foo", "bar");
			}
		};

		assertThatThrownBy(setMember).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	public void it_should_fail_to_remove_member() {
		ThrowingCallable removeMember = new ThrowingCallable() {
			@Override
			public void call() {
				partials.removeMember("foo");
			}
		};

		assertThatThrownBy(removeMember).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	public void it_should_fail_to_set_slot() {
		ThrowingCallable setSlot = new ThrowingCallable() {
			@Override
			public void call() {
				partials.setSlot(1, "foo");
			}
		};

		assertThatThrownBy(setSlot).isInstanceOf(UnsupportedOperationException.class);
	}
}
