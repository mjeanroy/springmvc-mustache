MustacheJava is a 100% java implementation of mustache templating and is supported out of the box by SpringMVC Mustache.

The documentation and source code can be found here: [https://github.com/spullara/mustache.java](https://github.com/spullara/mustache.java)

### Installation

If you are using springmvc-mustache >= 0.10.0, the recommended way is to add `springmvc-mustache-mustachejava` dependency:

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-mustachejava</artifactId>
  <version>0.10.0</version>
</dependency>
```

Otherwise, add required dependency:

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache</artifactId>
  <version>0.9.0</version>
</dependency>
<dependency>
  <groupId>com.github.spullara.mustache.java</groupId>
  <artifactId>compiler</artifactId>
  <version>0.8.17</version>
</dependency>
```

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

  private static final Logger log = LoggerFactory.getLogger(ExecutorServiceMustacheJavaCustomizer.class);

  @Override
  public void customize(MustacheFactory mustacheFactory) {
    if (mustacheFactory instanceof DefaultMustacheFactory) {
      ((DefaultMustacheFactory) mustacheFactory).setExecutorService(Executors.newSingleThreadExecutor());
    }
  }
}
```
