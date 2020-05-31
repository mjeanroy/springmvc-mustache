[![Build Status](https://travis-ci.org/mjeanroy/springmvc-mustache.svg?branch=master)](https://travis-ci.org/mjeanroy/springmvc-mustache)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/springmvc-mustache/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/springmvc-mustache)

SpringMVC Mustache allows you to render your mustache templates in your application using your favorite implementation:
- [JMustache](https://github.com/samskivert/jmustache)
- [Handlebars](https://github.com/jknack/handlebars.java)
- [MustacheJava](https://github.com/spullara/mustache.java)

For a complete documentation about Mustache Templating, feel free to check the full documentation available here: [https://mustache.github.io/](https://mustache.github.io/)

Rendering your mustache templates can now be done in two steps:

- Add required dependencies
- Add `@EnableMustache` annotation to your spring configuration

#### Required dependencies

##### Java >= 8

If you are using java < 8, please use version 2.0.0:

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache</artifactId>
  <version>1.0.0</version>
</dependency>

<!-- Add one of these dependencies. -->
<!-- This is the mustache implementation to use -->
<!-- You can add one of the following: -->
<dependency>
  <groupId>com.samskivert</groupId>
  <artifactId>jmustache</artifactId>
  <version>1.15</version>
</dependency>
<dependency>
  <groupId>com.github.jknack</groupId>
  <artifactId>handlebars</artifactId>
  <version>4.2.0</version>
</dependency>
<dependency>
  <groupId>com.github.spullara.mustache.java</groupId>
  <artifactId>compiler</artifactId>
  <version>0.9.6</version>
</dependency>
```

##### Java < 8

If you are using java < 8, please use version 1.0.0:

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache</artifactId>
  <version>1.0.0</version>
</dependency>

<!-- Add one of these dependencies. -->
<!-- This is the mustache implementation to use -->
<!-- You can add one of the following: -->
<dependency>
  <groupId>com.samskivert</groupId>
  <artifactId>jmustache</artifactId>
  <version>1.13</version>
</dependency>
<dependency>
  <groupId>com.github.jknack</groupId>
  <artifactId>handlebars</artifactId>
  <version>4.0.6</version>
</dependency>
<dependency>
  <groupId>com.github.spullara.mustache.java</groupId>
  <artifactId>compiler</artifactId>
  <version>0.8.17</version>
</dependency>
```

----

Starting with version `0.10.0`, it is recommended to only add `springmvc-mustache-[impl]` dependency, it add automatically the compatible
version of your favorite implementation.

For example:

```xml
<!-- Add one of these dependencies. -->
<!-- This is the mustache implementation to use -->
<!-- You can add one of the following: -->
<!-- Use version 1.0.0 if you need java < 8 compatibility -->
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-jmustache</artifactId>
  <version>2.0.0</version>
</dependency>
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-handlebars</artifactId>
  <version>2.0.0</version>
</dependency>
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-mustachejava</artifactId>
  <version>2.0.0</version>
</dependency>
```

#### Configuration

Now, you can add `@EnableMustache` to your spring configuration:

```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.github.mjeanroy.springmvc.view.mustache.configuration.EnableMustache;
import com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheProvider;

@Configuration
@EnableWebMvc
@EnableMustache
@ComponentScan("com.myApp")
public class SpringConfiguration {
}
```

Note that if you are using SpringBoot, **automatic configuration will be registered, you have nothing to do!**

By default, following configuration will be used:

- Prefix: `/templates`. This is the prefix used by spring view resolver to resolve view url.
- Suffix: `.template.html` This is the suffix used by spring view resolver to resolve view url.
- Cache: `true` By default, templates are automatically cached.
- View Names: `*` By default, all views are mapped using spring mustache view resolver.

If you want to override these settings, add a file named `mustache.properties` to your application:

```
##
# Override default mustache configuration.
##

mustache.prefix = /mustache
mustache.suffix = .mustache
mustache.cache = false
mustache.viewNames = *
```

You can configure following properties:

| Property                 | Type      | Default          | Description                                           |
| ------------------------ | --------- | ---------------- | ----------------------------------------------------- |
| `mustache.prefix`        | `String`  | `/templates/`    | View prefix.                                          |
| `mustache.suffix`        | `String`  | `.template.html` | View suffix.                                          |
| `mustache.cache`         | `Boolean` | `true`           | View cache.                                           |
| `mustache.viewNames`     | `String`  | `*`              | View name matcher.                                    |
| `mustache.defaultLayout` | `String`  |                  | The default template to used with the default layout. |

#### Layout & Partials

SpringMVC also supports layout: define one layout and apply it accross your views.

For example, suppose this layout:

{% raw %}
```html
<html>
  <head>
    <title>My Awesome Application</title>
  </head>
  <body>
    <div class="header">
      {{> header}}
    </div>
    <div class="content">
      {{> content}}
    </div>
    <div class="footer">
      {{> footer}}
    </div>
  </body>
</html>
```
{% endraw %}

Here, we define three partials:
- `header` and `footer`: it will be automatically loaded during rendering.
- `content` which is a special key used to render the "current" view (the view name set in your spring controller).

And now, this is how I will render the home page:

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FooController {

  @GetMapping("/")
  public ModelAndView fooView() {
    // Just render foo template, and view resolver will automatically replace content partials
    // by foo content
    ModelAndView view = new ModelAndView("home");
    view.addObject("name", "John Doe");
    return view;
  }
}
```

If you want to have different layouts for your views, just define some mappings that can be used to map view to layout templates:

```
# Define some layout mappings
# If mapping is define for a view, it will be used, otherwise default layout will be used
# Format is: [viewName]:[layoutName]
mustache.layoutMappings = admin1:secure ; admin2:secure

# Use default layout for other views
mustache.defaultLayout = index
```

And here is how I can render `admin1` view (using the `secure` layout template):

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FooController {

  @GetMapping("/")
  public ModelAndView admin1View() {
    // Just render foo template, and view resolver will automatically used "secure" template
    return new ModelAndView("admin1");
  }
}
```

#### Rendering

Now, you can easily render your templates:

Suppose you have this templates (say: `/templates/index.template.html`):

{% raw %}
```
Hello, my name is {{name}}
```
{% endraw %}

You can render your template with Spring MVC:

{% raw %}
```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FooController {

  @GetMapping("/")
  public ModelAndView fooView() {
    ModelAndView view = new ModelAndView("index");
    view.addObject("name", "John Doe");
    return view;
  }
}
```
{% endraw %}

#### SpringBoot

Finally, here is an exemple using Spring Boot:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@EnableAutoConfiguration(exclude = MustacheAutoConfiguration.class)
@Controller
public class Application {

    // Render a simple template
    @GetMapping("/")
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

Note that since `Spring-Boot` provide autoconfiguration for `JMustache`, you will need to excluce this one explicitly to avoid conflict (but this is not needed you are using Handlebars or MustacheJava implementation).

#### XML Configuration

If you prefer XML configuration, you may want to use one of these (depends on the underlying implementation) :

**JMustache:**

```xml
<!-- Create mustache beans -->
<bean id="jmustache" class="com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheCompilerFactoryBean" />
<bean id="mustacheTemplateLoader" class="com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheTemplateLoaderFactoryBean" />
<bean id="mustacheCompiler" class="com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheCompilerFactoryBean" />

<!-- Create view resolver -->
<bean class="com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver">
    <constructor-arg ref="mustacheCompiler" />
    <property name="order" value="1" />
    <property name="defaultLayout" value="layout" />
    <property name="prefix" value="templates/" />
    <property name="suffix" value=".template.html" />
</bean>
```

**Handlebars:**

```xml
<!-- Create mustache beans -->
<bean id="handlebars" class="com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsFactoryBean" />
<bean id="mustacheTemplateLoader" class="com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheTemplateLoaderFactoryBean" />
<bean id="mustacheCompiler" class="com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheCompilerFactoryBean" />

<!-- Create view resolver -->
<bean class="com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver">
    <constructor-arg ref="mustacheCompiler" />
    <property name="order" value="1" />
    <property name="defaultLayout" value="layout" />
    <property name="prefix" value="templates/" />
    <property name="suffix" value=".template.html" />
</bean>
```

**MustacheJava:**

```xml
<!-- Create mustache beans -->
<bean id="mustacheTemplateLoader" class="com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheTemplateLoaderFactoryBean" />

<bean id="mustacheFactory" class="com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheFactory">
    <constructor-arg ref="mustacheTemplateLoader"/>
</bean>

<bean id="mustacheResolver" class="com.github.mjeanroy.springmvc.view.mustache.mustachejava.SpringMustacheResolver">
    <constructor-arg ref="mustacheTemplateLoader"/>
</bean>

<bean id="mustacheCompiler" class="com.github.mjeanroy.springmvc.view.mustache.configuration.MustacheCompilerFactoryBean" />

<!-- Create view resolver -->
<bean class="com.github.mjeanroy.springmvc.view.mustache.MustacheViewResolver">
    <constructor-arg ref="mustacheCompiler" />
    <property name="order" value="1" />
    <property name="defaultLayout" value="layout" />
    <property name="prefix" value="templates/" />
    <property name="suffix" value=".template.html" />
</bean>
```