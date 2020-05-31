MustacheJava is a 100% java implementation of mustache templating and is supported out of the box by SpringMVC Mustache.

The documentation and source code can be found here: [https://github.com/spullara/mustache.java](https://github.com/spullara/mustache.java)

### Installation

If you are using springmvc-mustache >= 0.10.0, the recommended way is to add `springmvc-mustache-mustachejava` dependency:

```xml
<!-- Use version 1.0.0 if you need java <= 7 compatibility -->
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-mustachejava</artifactId>
  <version>2.0.0</version>
</dependency>
```

Otherwise, add required dependency:

```xml
<!-- Use version 1.x.x if you need java <= 7 compatibility -->
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache</artifactId>
  <version>2.0.0</version>
</dependency>

<!-- Use version 0.8.17 if you use springmvc-mustache <= 1.x.x -->
<dependency>
  <groupId>com.github.spullara.mustache.java</groupId>
  <artifactId>compiler</artifactId>
  <version>0.9.6</version>
</dependency>
```

**Note that springmvc-mustache <= 1.x.x is compatible with mustachejava@0.8.x and springmvc-mustache >= 2.x.x is compatible with mustachejava@0.9.x.**

### Configuration

Now, you can add @EnableMustache to your spring configuration:

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

Note that you can configure following properties (not specific to MustacheJava implementation):

| Property                 | Type      | Default          | Description                                           |
| ------------------------ | --------- | ---------------- | ----------------------------------------------------- |
| `mustache.prefix`        | `String`  | `/templates/`    | View prefix.                                          |
| `mustache.suffix`        | `String`  | `.template.html` | View suffix.                                          |
| `mustache.cache`         | `Boolean` | `true`           | View cache.                                           |
| `mustache.viewNames`     | `String`  | `*`              | View name matcher.                                    |
| `mustache.defaultLayout` | `String`  |                  | The default template to used with the default layout. |

### MustacheJava customization

If you want to configure MustacheJava compiler, you can add the following properties to your configuration:

| Property                               | Type      | Description                           |
| -------------------------------------- | --------- | ------------------------------------- |
| `mustache.mustachejava.recursionLimit` | `Integer` | Maximum recursion limit for partials. |

Otherwise, you can register a bean implementing `com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaCustomizer`, it will be automatically executed when compiler is created.

For example, if you want to set a custom `ExecutorService` for asynchronous rendering:

```java
import com.github.mjeanroy.springmvc.view.mustache.configuration.mustachejava.MustacheJavaCustomizer;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
class ExecutorServiceMustacheJavaCustomizer implements MustacheJavaCustomizer {

  @Override
  public void customize(MustacheFactory mustacheFactory) {
    if (mustacheFactory instanceof DefaultMustacheFactory) {
      ((DefaultMustacheFactory) mustacheFactory).setExecutorService(Executors.newSingleThreadExecutor());
    }
  }
}
```
