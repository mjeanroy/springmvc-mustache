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

package com.github.mjeanroy.springmvc.view.mustache.core;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Collection;

import static com.github.mjeanroy.springmvc.view.mustache.tests.ReflectionTestUtils.readField;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompositeResourceLoaderTest {

	private ResourceLoader resourceLoader1;
	private ResourceLoader resourceLoader2;

	private CompositeResourceLoader compositeResourceLoader;

	@Before
	public void setUp() {
		resourceLoader1 = mock(ResourceLoader.class);
		resourceLoader2 = mock(ResourceLoader.class);
		compositeResourceLoader = new CompositeResourceLoader(asList(
				resourceLoader1,
				resourceLoader2
		));
	}

	@Test
	public void it_should_create_resource_loader_from_collection() {
		Collection<ResourceLoader> collection = asList(resourceLoader1, resourceLoader2);
		CompositeResourceLoader compositeResourceLoader = new CompositeResourceLoader(collection);

		Collection<ResourceLoader> resourceLoaders = readField(compositeResourceLoader, "resourceLoaders");

		assertThat(resourceLoaders).isNotNull().isNotEmpty()
				.isNotSameAs(collection)
				.hasSize(2).containsOnly(resourceLoader1, resourceLoader2);
	}

	@Test
	public void it_should_find_first_resource() {
		Resource r1 = newResource(true);
		Resource r2 = newResource(false);

		String location = "foo";
		when(resourceLoader1.getResource(location)).thenReturn(r1);
		when(resourceLoader2.getResource(location)).thenReturn(r2);

		Resource resource = compositeResourceLoader.getResource(location);

		assertThat(resource).isNotNull().isSameAs(r1);
		verify(resourceLoader1).getResource(location);
		verify(resourceLoader2, never()).getResource(location);
	}

	@Test
	public void it_should_find_second_resource() {
		Resource r1 = newResource(false);
		Resource r2 = newResource(true);

		String location = "foo";
		when(resourceLoader1.getResource(location)).thenReturn(r1);
		when(resourceLoader2.getResource(location)).thenReturn(r2);

		Resource resource = compositeResourceLoader.getResource(location);

		assertThat(resource).isNotNull().isSameAs(r2);
		verify(resourceLoader1).getResource(location);
		verify(resourceLoader2).getResource(location);
	}

	@Test
	public void it_should_not_find_resource() {
		Resource r1 = newResource(false);
		Resource r2 = newResource(false);

		String location = "foo";
		when(resourceLoader1.getResource(location)).thenReturn(r1);
		when(resourceLoader2.getResource(location)).thenReturn(r2);

		Resource resource = compositeResourceLoader.getResource(location);

		assertThat(resource).isNotNull().isSameAs(r2);
		verify(resourceLoader1).getResource(location);
		verify(resourceLoader2).getResource(location);
	}

	private Resource newResource(boolean exists) {
		Resource resource = mock(Resource.class);
		when(resource.exists()).thenReturn(exists);
		return resource;
	}
}
