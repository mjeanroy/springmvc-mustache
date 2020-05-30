JMustache is a 100% java implementation of mustache templating and is supported out of the box by SpringMVC Mustache.

### Installation

If you are using springmvc-mustache >= 0.10.0, the recommended way is to add `springmvc-mustache-jmustache` dependency:

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-jmustache</artifactId>
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
  <groupId>com.samskivert</groupId>
  <artifactId>jmustache</artifactId>
  <version>1.13</version>
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

Note that you can configure following properties (not specific to JMustache implementation):

| Property                 | Type      | Default          | Description                                           |
| ------------------------ | --------- | ---------------- | ----------------------------------------------------- |
| `mustache.prefix`        | `String`  | `/templates/`    | View prefix.                                          |
| `mustache.suffix`        | `String`  | `.template.html` | View suffix.                                          |
| `mustache.cache`         | `Boolean` | `true`           | View cache.                                           |
| `mustache.viewNames`     | `String`  | `*`              | View name matcher.                                    |
| `mustache.defaultLayout` | `String`  |                  | The default template to used with the default layout. |

### JMustache customization

If you want to configure JMustache compiler, you can add the following properties to your configuration:

| Property                                | Type      | Description                                                                                                 |
| --------------------------------------- | --------- | ----------------------------------------------------------------------------------------------------------- |
| `mustache.jmustache.nullValue`          | `String`  | A value to use when a variable resolves to `null`.                                                          |
| `mustache.jmustache.defaultValue`       | `String`  | Use the given value for any variable that is missing, or otherwise resolves to `null`.                      |
| `mustache.jmustache.emptyStringIsFalse` | `Boolean` | If this value is `true`, empty string will be treated as a falsy, as in JavaScript mustache implementation. |
| `mustache.jmustache.zeroIsFalse`        | `Boolean` | If this value is `true`, zero will be treated as a falsy value, as in JavaScript mustache implementation.   |
| `mustache.jmustache.escapeHTML`         | `Boolean` | Does or does not escape HTML by default.                                                                    |
| `mustache.jmustache.strictSections`     | `Boolean` | Whether or not to throw an exception when a section resolves to a missing value.                            |
| `mustache.jmustache.standardsMode`      | `Boolean` | Whether or not standards mode is enabled.                                                                   |

Otherwise, you can register a bean implementing `com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheCustomizer`, it will be automatically executed when mustache compiler is created.

For example, if you want to configure a custom formatter:

```java
import com.github.mjeanroy.springmvc.view.mustache.configuration.jmustache.JMustacheCustomizer;
import com.samskivert.mustache.Mustache;
import org.springframework.stereotype.Component;

@Component
class CustomFormatterJMustacheCustomizer implements JMustacheCustomizer {

  @Override
  public Mustache.Compiler customize(Mustache.Compiler compiler) {
    return compiler.withFormatter(new Mustache.Formatter() {
      @Override
      public String format(Object value) {
        return value == null ? null : value.toString();
      }
    });
  }
}
```
