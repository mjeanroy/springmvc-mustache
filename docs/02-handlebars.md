Handlebars is a 100% java implementation of mustache templating and is supported out of the box by SpringMVC Mustache. Note that handlebars can do more than
strict mustache templating and it is your choice to use it or not.

The documentation and source code can be found here: [https://github.com/jknack/handlebars.java](https://github.com/jknack/handlebars.java)

### Installation

If you are using springmvc-mustache >= 0.10.0, the recommended way is to add `springmvc-mustache-handlebars` dependency:

```xml
<!-- Use version 1.0.0 if you need java <= 7 compatibility -->
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache-handlebars</artifactId>
  <version>2.0.0</version>
</dependency>
```

Otherwise, add required dependency:

```xml
<!-- Use version 1.0.0 if you need java <= 7 compatibility -->
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>springmvc-mustache</artifactId>
  <version>2.0.0</version>
</dependency>

<!-- Use version 4.0.6 if you need java <= 7 compatibility -->
<dependency>
  <groupId>com.github.jknack</groupId>
  <artifactId>handlebars</artifactId>
  <version>4.2.0</version>
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

Note that you can configure following properties (not specific to Handlebars implementation):

| Property                 | Type      | Default          | Description                                           |
| ------------------------ | --------- | ---------------- | ----------------------------------------------------- |
| `mustache.prefix`        | `String`  | `/templates/`    | View prefix.                                          |
| `mustache.suffix`        | `String`  | `.template.html` | View suffix.                                          |
| `mustache.cache`         | `Boolean` | `true`           | View cache.                                           |
| `mustache.viewNames`     | `String`  | `*`              | View name matcher.                                    |
| `mustache.defaultLayout` | `String`  |                  | The default template to used with the default layout. |

### Handlebars customization

If you want to configure Handlebars compiler, you can add the following properties to your configuration:

| Property                                      | Type      | Description                                                                                                                                                 |
| --------------------------------------------- | --------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `mustache.handlebars.startDelimiter`          | `String`  | Handlebars start delimiter, default is `{{`.                                                                                                                |
| `mustache.handlebars.endDelimiter`            | `String`  | Handlebars end delimiter, default is `}}`.                                                                                                                  |
| `mustache.handlebars.stringParams`            | `Boolean` | If `true`, missing helper parameters will be resolve to their names.                                                                                        |
| `mustache.handlebars.deletePartialAfterMerge` | `Boolean` | If `true`, templates will be deleted once applied.                                                                                                          |
| `mustache.handlebars.infiniteLoops`           | `Boolean` | If `true`, templates will be able to call him self directly or indirectly                                                                                   |
| `mustache.handlebars.parentScopeResolution`   | `Boolean` | Set to `true`, if you want to extend lookup to parent scope, like Mustache Spec, or  `false`, if lookup is restricted to current scope, like handlebars.js. |
| `mustache.handlebars.prettyPrint`             | `Boolean` | If `true`, unnecessary spaces and new lines will be removed from output.                                                                                    |

Otherwise, you can register a bean implementing `com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsCustomizer`, it will be automatically executed when mustache compiler is created.

For example, if you want to register custom handlebars helpers:

```java
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.mjeanroy.springmvc.view.mustache.configuration.handlebars.HandlebarsCustomizer;
import org.springframework.stereotype.Component;

@Component
class StringHelpersHandlebarsCustomizer implements HandlebarsCustomizer {

  @Override
  public void customize(Handlebars handlebars) {
    StringHelpers.register(handlebars);
  }
}
```
