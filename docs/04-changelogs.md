##### 0.10.0

- Add specific artifact with dependencies:
  - `springmvc-mustache-jmustache` defined with jmustache@1.13
  - `springmvc-mustache-handlebars` defined with handlebars@4.0.6
  - `springmvc-mustache-mustachejava` defined with mustachejava@0.8.17

##### 0.9.0

- Add an option to fix charset of template loader ([#872e02e](https://github.com/mjeanroy/springmvc-mustache/commit/872e02e5d4f94d67d4025403187986407881f514))
- Fix compatibility with handlebars >= 4.1.0 ([#48ec388](https://github.com/mjeanroy/springmvc-mustache/commit/48ec38862f280d885a4e08603694bcdb23dc4dd4)).

##### 0.8.0

- Support custom implementation registered using the ServiceProvider Interface (SPI) ([#5c1cb6b](https://github.com/mjeanroy/springmvc-mustache/commit/5c1cb6bef5066c5d8bee2940e0c1e625361e6a66))

##### 0.7.0

- Add a way to customize mustachejava factory via `MustacheJavaCustomizer` ([#e7f2cd0](https://github.com/mjeanroy/springmvc-mustache/commit/e7f2cd00b84f9b7b4cfe03b3db39eeb186f390f9)).
- Add a way to customize handlebars factory via `HandlebarsCustomizer` ([#83f083b](https://github.com/mjeanroy/springmvc-mustache/commit/83f083bfb6db4d0b5740d98c6d7352beb7ad860b)).
- Add a way to customize handlebars factory via `JMustacheCustomizer` ([#be68c54](https://github.com/mjeanroy/springmvc-mustache/commit/be68c54337612d4446aa2b474d08990924d1b4b4)).
- Add configuration for jmustache compiler ([#ca5f91d](https://github.com/mjeanroy/springmvc-mustache/commit/ca5f91d8b7eb51437438d7d018b33f0c5a86fcf7))
- Add configuration for handlebars compiler ([#17b3b2f](https://github.com/mjeanroy/springmvc-mustache/commit/17b3b2f279d3311ff3fa850807b95cddc0dc42de))
- Add configuration for mustachejava factory ([#9689d47](https://github.com/mjeanroy/springmvc-mustache/commit/9689d47c364402a9b4c996c0dfc92a7d8319ae8c))

##### 0.6.0

- Add automatic module name ([#cf1f25e](https://github.com/mjeanroy/springmvc-mustache/commit/cf1f25ec5e033e614b0f09850eb46bec9edbb141))
- Add JSP taglib ([#cba3d5b](https://github.com/mjeanroy/springmvc-mustache/commit/cba3d5b667a90bf9bd5c840353fe168ede3f4525))
- Deprecate Nashorn.
- CI: add missing jdk.
- Dependency Updates.

##### 0.5.3

- Dependency Updates.

##### 0.5.2

- Fix dependency scope, powermock must be a test dependency.

##### 0.5.1

- Fix dependency scope, assertj must be a test dependency.

##### 0.5.0

- Implement Nashorn Provider ([#0280cd4](https://github.com/mjeanroy/springmvc-mustache/commit/0280cd482cbc909d4c5ecfebc18cfc1753aa3882))

#### 0.4.0

- Add factory bean to simplify XML configuration ([#22ff717](https://github.com/mjeanroy/springmvc-mustache/commit/22ff71713a8945423a8c2bb2b3305495bdbc6394))
- Dependency Updates.
- Add XML samples.

#### 0.3.0

- Add spring boot autoconfiguration ([#e983323](https://github.com/mjeanroy/springmvc-mustache/commit/e983323c83a0cad81e83571f4a1a4a4b7f1f1234))

#### 0.2.2

- Fix a bug when loading templates with mustachejava ([#75b5ed4](https://github.com/mjeanroy/springmvc-mustache/commit/75b5ed47f2d656122c7e58cb12e675e727be6d7e))
- Fix a bug with zero not being interpreted as a falsy value with mustachejava ([#248d781](https://github.com/mjeanroy/springmvc-mustache/commit/248d781e18eccd8dd83d3a2427f62a208410221c))
- Dependency Updates.

#### 0.2.1

- Try to load multiple templates from multiple locations ([#08c1a8e](https://github.com/mjeanroy/springmvc-mustache/commit/08c1a8e2eb531b2a196f761130f73f58a175ff04))
- Use consistent exceptions ([#34920bc](https://github.com/mjeanroy/springmvc-mustache/commit/34920bc431d912918fb3bba6df2c4b8bbdf16610))

#### 0.2.0

- Implement handlebars provider.
- Implement mustachejava provider.
- Add samples.
- Dependency Updates.

#### 0.1.1

- Add configuration options ([#626199b](https://github.com/mjeanroy/springmvc-mustache/commit/626199b07bb342766af896ce23e75ea62b5ec304))

#### 0.1.0

- Implement JMustache renderer.
