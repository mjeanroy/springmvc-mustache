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

package com.github.mjeanroy.springmvc.view.mustache.commons.reflection;

import com.github.mjeanroy.springmvc.view.mustache.exceptions.ReflectionException;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static com.github.mjeanroy.springmvc.view.mustache.commons.lang.PreConditions.hasText;

/**
 * Commons static class utilities.
 */
public final class Classes {

	private Classes() {
	}

	/**
	 * Check that a given class is available on
	 * classpath.
	 *
	 * @param klass Class name.
	 * @return True if class is available, false otherwise.
	 */
	public static boolean isPresent(String klass) {
		hasText(klass, "Class name must not be empty");
		try {
			Class.forName(klass);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Get class object related to given class name.
	 *
	 * @param klass Class name.
	 * @return The class object.
	 */
	public static Class<?> getClassOf(String klass) {
		hasText(klass, "Class name must not be empty");
		try {
			return Class.forName(klass);
		}
		catch (ClassNotFoundException ex) {
			throw new ReflectionException(ex);
		}
	}

	/**
	 * Create new class instance.
	 * @param klassName The class name.
	 * @param argTypes Argument types, used to retrieve appropriate constructor.
	 * @param args Arguments.
	 * @return The new instance.
	 * @throws ReflectionException If an error occurred during class instantiation.
	 */
	public static Object newInstance(String klassName, Class<?>[] argTypes, Object[] args) {
		try {
			Class<?> klass = Class.forName(klassName);
			return newInstance(klass, argTypes, args);
		}
		catch (ClassNotFoundException ex) {
			throw new ReflectionException(ex);
		}
	}

	/**
	 * Invoke given method on given instance with given arguments.
	 * @param instance Object instance.
	 * @param methodName Method to invoke.
	 * @param parameterTypes Type of parameters.
	 * @param args Method arguments.
	 * @param <T> Type of given instance.
	 * @return Method invocation result.
	 */
	public static <T> Object invoke(T instance, String methodName, Class<?>[] parameterTypes, Object[] args) {
		Class<?> klass = instance.getClass();

		try {
			Method method = klass.getMethod(methodName, parameterTypes);
			return method.invoke(instance, args);
		}
		catch (NoSuchMethodException ex) {
			throw new ReflectionException(ex);
		}
		catch (IllegalAccessException ex) {
			throw new ReflectionException(ex);
		}
		catch (InvocationTargetException ex) {
			throw new ReflectionException(ex);
		}
	}

	/**
	 * Create new class instance.
	 * @param klass The class.
	 * @param <T> The created type instance.
	 * @return The new instance.
	 * @throws ReflectionException If an error occurred during class instantiation.
	 */
	private static <T> T newInstance(Class<T> klass, Class<?>[] argTypes, Object[] args) {
		try {
			Constructor<T> ctor = klass.getConstructor(argTypes);
			return ctor.newInstance(args);
		}
		catch (NoSuchMethodException ex) {
			throw new ReflectionException(ex);
		}
		catch (IllegalAccessException ex) {
			throw new ReflectionException(ex);
		}
		catch (InvocationTargetException ex) {
			throw new ReflectionException(ex);
		}
		catch (InstantiationException ex) {
			throw new ReflectionException(ex);
		}
	}

	/**
	 * Get annotation method value.
	 *
	 * @param importingClassMetadata Metadata.
	 * @param annotationClass        Annotation class to look for.
	 * @param name                   Name of method.
	 * @param defaultValue           Default value if original value is null.
	 * @param <T>                    Type of returned value.
	 * @return Annotation value, or default value if original value is null.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAnnotationValue(AnnotationMetadata importingClassMetadata, Class annotationClass, String name, T defaultValue) {
		Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(annotationClass.getName());
		return attributes != null && attributes.containsKey(name) ? (T) attributes.get(name) : defaultValue;
	}
}
