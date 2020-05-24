package com.github.mjeanroy.springmvc.mustache.sample.mustachejava.mustachejava;

import com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaCustomizer;
import com.github.mjeanroy.springmvc.view.mustache.logging.Logger;
import com.github.mjeanroy.springmvc.view.mustache.logging.LoggerFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
class ExecutorServiceMustacheJavaCustomizer implements MustacheJavaCustomizer {

	private static final Logger log = LoggerFactory.getLogger(ExecutorServiceMustacheJavaCustomizer.class);

	@Override
	public void customize(MustacheFactory mustacheFactory) {
		log.info("Customizing executor service of mustachejava factory");
		if (mustacheFactory instanceof DefaultMustacheFactory) {
			((DefaultMustacheFactory) mustacheFactory).setExecutorService(Executors.newSingleThreadExecutor());
		}
	}
}
