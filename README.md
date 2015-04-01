# SpringMVC-Mustache
---------------------

Simple library that will allow you to use a mustache templates with your SpringMVC application.

Note: this library use jmustache, handlebars or mustache.java under the hood.

## Installation

With Maven, add explicit dependency:

```xml
    <dependency>
        <groupId>com.github.mjeanroy</groupId>
        <artifactId>springmvc-mustache</artifactId>
        <version>0.3.0</version>
    </dependency>

    <!-- Add mustache implementation to use -->
    <!-- You can add one of the following: -->
    <dependency>
        <groupId>com.samskivert</groupId>
        <artifactId>jmustache</artifactId>
        <version>1.9</version>
    </dependency>
    <dependency>
        <groupId>com.github.jknack</groupId>
        <artifactId>handlebars</artifactId>
        <version>1.3.2</version>
    </dependency>
    <dependency>
        <groupId>com.github.spullara.mustache.java</groupId>
        <artifactId>compiler</artifactId>
        <version>0.8.16</version>
    </dependency>
```

## Configuration

Configuration is very easy, add `@EnableMustache` annotation to your configuration and you're done!

Default configuration will try to detect mustache implementation on classpath (jmustache or handlebars), but you can specify
which implementation you want to use:

| Options  | Value         |                                                                               |
|----------|---------------|-------------------------------------------------------------------------------|
| provider | AUTO          | Use classpath detection to select the implementation. This is the default.    |
|          | JMUSTACHE     | Use jmustache implementation. JMustache dependency must be available.         |
|          | HANDLEBAR     | Use handlebar implementation. Handlebar dependency must be available.         |
|          | MUSTACHE_JAVA | Use mustache.java implementation. Mustache.java dependency must be available. |

```java
package com.myApp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.github.mjeanroy.springmvc.view.mustache.configuration.EnableMustache;
import com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheProvider;

@Configuration
@EnableWebMvc
@EnableMustache(provider = MustacheProvider.AUTO)
@ComponentScan("com.myApp")
public class SpringConfiguration {
}
```

If you are using Spring Boot, then configuration will be automatically detected. Note that if you choose to use jmustache as implementation, then you will probably have to disable native spring boot mustache configuration.

Here is an exemple using Spring Boot:

```java
package com.myApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@EnableAutoConfiguration(exclude = MustacheAutoConfiguration.class)
@Controller
public class Application {

	// Render a simple template
        @RequestMapping("/")
        public ModelAndView fooView() {
                ModelAndView modelAndView = new ModelAndView("foo");
                modelAndView.addObject("name", "foo");
                return modelAndView;
        }

        public static void main(String[] args) throws Exception {
                SpringApplication.run(Application.class, args);
        }
}

```

By default, following configuration will be used:

- Prefix: '/templates'.
  This is the prefix used by spring view resolver to resolve view url.
- Suffix: '.template.html'
  This is the suffix used by spring view resolver to resolve view url.
- Cache: true
  By default, templates are automatically cached.
- View Names: '*'
  By default, all views are mapped using spring mustache view resolver.

If you want to override these settings, add a file named `mustache.properties` to your application:

```
##
# Override default mustache configuration.
##

mustache.prefix = /mustache
mustache.suffix = .mustache
mustache.cache = false
mustache.viewNames = *

# --
# See later what are these properties
mustache.defaultLayout = index
mustache.layoutKey = content
mustache.layoutMappings = admin1:secure ; admin2:secure
```

## Render mustache template

Now, you can easily render your templates:

Suppose you have this templates (say: '/templates/foo.template.html'):

```
Hello, my name is {{name}}
```

You can render your template with Spring MVC:

```java
package com.myApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FooController {

  @RequestMapping(value = "/foo", method = RequestMethod.GET)
  public ModelAndView fooView() {
    ModelAndView view = new ModelAndView("foo");
    view.addObject("name", "foo");
    return view;
  }
}
```

Now, go to your url and you should see the result!

## Partials and Layout

### Partials

This implementation can be used to render view with partials.

Suppose, you have the following views:

/templates/index.template.html:

```
<div class="header">
  {{> header}}
</div>
<div class="content">
  Hello, my name is {{name}}
</div>
```

/templates/header.template.html:
```
<div class="menu">
  <a href="/foo">FOO</a>
  <a href="/bar">BAR</a>
</div>
```

```java
package com.myApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView fooView() {
    ModelAndView view = new ModelAndView("index");
    view.addObject("name", "foo");
    return view;
  }
}
```

You can render index view, and view resolver will automatically try to resolve header template and inject it into the view!

### Layout

Suppose you have an index file that define header and footer and you don't want to declare your layout everywhere. With this view resolver, you can
define your layout and it will be used to render views.

By default, this feature is disabled and you will have to explicitly declare your layout file name using a mustache property file in your classpath.

Views will be rendered as an inner partials using "content" key:

mustache.properties:

```
mustache.defaultLayout = layout

# --
# Default value for this property is 'content' but it can be change
mustache.layoutKey = content
```

/templates/layout.template.html:

```
<div class="header">
  {{> header}}
</div>
<div class="content">
  {{> content}}
</div>
<div class="footer">
  {{> footer}}
</div>
```

/templates/header.template.html:

```
<div class="menu">
  <a href="/foo">FOO</a>
  <a href="/bar">BAR</a>
</div>
```

/templates/footer.template.html:

```
<div class="copyright">
  Copyright 2014
</div>
```

/templates/foo.template.html:

```
Hello, my name is {{name}}
```

```java
package com.myApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FooController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView fooView() {
    // Just render foo template, and view resolver will automatically replace content partials
    // by foo content
    ModelAndView view = new ModelAndView("foo");
    view.addObject("name", "foo");
    return view;
  }
}
```

### Layout mappings

If you want to have different layouts for your views, just define some mappings that can be used to map view to layout templates:


mustache.properties:

```
# Define some layout mappings
# If mapping is define for a view, it will be used, otherwise default layout will be used
# Format is: [viewName]:[layoutName]
mustache.layoutMappings = admin1:secure ; admin2:secure

# Use default layout for other views
mustache.defaultLayout = index
```

```java
package com.myApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FooController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView admin1View() {
    // Just render foo template, and view resolver will automatically used "secure" template
    return new ModelAndView("admin1");
  }
}
```

## Samples

If you clone the repository, you will find samples using:

- JMustache
- Mustache.java
- Handlebars
- Spring boot with JMustache implementation.

These samples are really simple (render a really simple template with partial). Do not hesitate to submit your sample if you want.

## Licence

MIT License (MIT)

## Contributing

If you found a bug or you thing something is missing, feel free to contribute and submit an issue or a pull request.
