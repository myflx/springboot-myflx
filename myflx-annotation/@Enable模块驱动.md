``@EnableXX`` 模块驱动，实现了按需装配，同时屏蔽了功能实现的细节，简化了开发装配的步骤。

| 框架             | @Enable 注解模块               | 激活模块            |
| ---------------- | ------------------------------ | ------------------- |
| Spring Framework | @EnableWebMvc                  | WebMvc 模块         |
|                  | @EnableTransactionManangement  | 事务管理模块        |
|                  | @EnableCacheing                | Caching 模块        |
|                  | @EnableMBeanExport             | 激活JMX 模块        |
|                  | @EnableWebFlux                 | WebFlux 模块        |
|                  | @EnableAsync                   | 异步处理模块        |
|                  | @EnableAspectJAutoProxy        | AspectJ 代理模块    |
| Spring Boot      | @EnableAutoConfiguration       | 自动装配模块        |
|                  | @EnableManagementContext       | Actuator 管理模块   |
|                  | @EnableConfigurationProperties | 配置属性绑定模块    |
|                  | @EnableOAuth2Sso               | OAuth2 单点登录模块 |
| Spring Cloud     | @EnableEurekaServer            | Eureka服务器模块    |
|                  | @EnableConfigServer            | 配置服务器模块      |
|                  | @EnableFeignClients            | Feign客户端模块     |
|                  | @EnableZuulProxy               | 服务网关Zuul模块    |
|                  | @EnableCircuitBreaker          | 服务熔断模块        |

