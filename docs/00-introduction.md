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

Add `springmvc-mustache` and your favorite implementation to your `pom.xml`:

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache</artifactId>
  <version>0.10.0</version>
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

Starting with version `0.10.0`, it is recommended to only add `springmvc-mustache-[impl]` dependency, it add automatically the compatible
version of your favorite implementation.

For example:

```xml
<!-- Add one of these dependencies. -->
<!-- This is the mustache implementation to use -->
<!-- You can add one of the following: -->
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-jmustache</artifactId>
  <version>0.10.0</version>
</dependency>
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-handlebars</artifactId>
  <version>0.10.0</version>
</dependency>
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-mustachejava</artifactId>
  <version>0.10.0</version>
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

# --
# See later what are these properties
mustache.defaultLayout = index
mustache.layoutKey = content
mustache.layoutMappings = admin1:secure ; admin2:secure
```

#### Rendering

Now, you can easily render your templates:

Suppose you have this templates (say: `/templates/index.template.html`):

```
Hello, my name is {{name}}
You can render your template with Spring MVC:
```

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
````
