 在**

## '



## '``@EnableXX`` 模块驱动

​		实现了按需装配，同时屏蔽了功能实现的细节，简化了开发装配的步骤。

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



### ``@EnableWebMvc`` 

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}
```

#### 代理配置

:``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration`` 

是代理WebMvc配置的类，代理配置为聚合配置``WebMvcConfigurerComposite`` (持有配置`WebMvcConfigurer`列表)，通过自动注入的方式注入上下文应用中的所有配置类。其他方法基本为WebMvcConfigurerComposite 的代理操作。

```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
	private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
	@Autowired(required = false)
	public void setConfigurers(List<WebMvcConfigurer> configurers) {
		if (!CollectionUtils.isEmpty(configurers)) {
			this.configurers.addWebMvcConfigurers(configurers);
		}
	}
    //...
}    
```

- ``org.springframework.web.servlet.config.annotation.PathMatchConfigurer`` 

  处理器隐射的帮助类。

- ``org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer``

  内容协商配置类，创建内容协商管理器，同时配置各种内容协商策略。

- ``org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer``

  处理异步请求支持的配置器，配置执行器，超时时间，请求拦截器，结果拦截器。

- ``org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer``

  处理默认Servlet处理器`DefaultServletHttpRequestHandler`的配置的配置类。

- ``org.springframework.format.FormatterRegistry``

  字段格式化逻辑（Formatter）的注册

- ``org.springframework.web.servlet.config.annotation.InterceptorRegistry``

  主要用户注册拦截器。

  - 拦截器``org.springframework.web.servlet.HandlerInterceptor`` 会被包装成

  ``InterceptorRegistration registration = new InterceptorRegistration(interceptor);`` 然后被添加在：``org.springframework.web.servlet.config.annotation.InterceptorRegistry#registrations`` 列表中。

  ```java
  public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
  		InterceptorRegistration registration = new InterceptorRegistration(interceptor);
  		this.registrations.add(registration);
  		return registration;
  	}
  ```

  - 拦截器``org.springframework.web.context.request.WebRequestInterceptor`` 的添加

    ```java
    public InterceptorRegistration addWebRequestInterceptor(WebRequestInterceptor interceptor) {
    		WebRequestHandlerInterceptorAdapter adapted = new 		WebRequestHandlerInterceptorAdapter(interceptor);
    		InterceptorRegistration registration = new InterceptorRegistration(adapted);
    		this.registrations.add(registration);
    		return registration;
    	}
    ```

    - 拦截器顺序

      通过接口 ``Ordered`` ,``PriorityOrdered`` 实现
      
    - 默认拦截器
    
      - ``org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor`` 
    
        拦截每个请求，将类型转换服务类暴露在每个请求中。ConversionService 持有大量转换器（124个）用于数据转换，实现类为:
    
      ​         ``org.springframework.boot.autoconfigure.web.format.WebConversionService``
    
      - ``org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor``
    
        ​    将资源地址提供者对象暴露在每个请求中，获取静态资源路径的处理器。

- ``org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry``

  主要用于注册资源处理器来处理静态资源。

- ``org.springframework.web.servlet.config.annotation.CorsRegistry``

  用于出来跨域请求路径的配置。

- ``org.springframework.web.servlet.config.annotation.ViewControllerRegistry``

  主要用于视图控制器的注册。

- ``org.springframework.web.servlet.config.annotation.ViewResolverRegistry``

  主要用于视图处理器的注册。

- ``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#addArgumentResolvers ``

  ``HandlerMethodArgumentResolver``策略接口类：将请求的参数隐射到方法的参数中。

- ``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#addReturnValueHandlers``

  ``HandlerMethodReturnValueHandler``策略接口类：处理调用返回值的一种策略。

- ``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#configureMessageConverters`

  ``HttpMessageConverter`` HttpMessage的转换器，策略接口类（Strategy interface that specifies a converter that can convert from and to HTTP requests and responses.）

- ``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#extendMessageConverters``

  消息处理器的扩展入口。

- ``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#configureHandlerExceptionResolvers``

  HandlerExceptionResolver：异常处理器，子类继承用于处理异常到对应的模型视图对象。

- ``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#extendHandlerExceptionResolvers``

  异常处理器扩展入口。

- ``org.springframework.validation.MessageCodesResolver`` 消息code处理器。

  默认实现：``org.springframework.validation.DefaultMessageCodesResolver``

  

- HttpMessage，MediaType

#### 配置实现

``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport`` 持有 应用上下文，Servlet上下文。

##### 注入实例

1. ``org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping`` 

> 主要用于解析注解了@Controller，@RequestMapping 对应的类用来，从Class和Method两个层面来创建并合并请求的隐射信息实例即`RequestMappingInfo` 。

2. ``org.springframework.util.PathMatcher``

   路径匹配器，唯一实现为AntPathMatcher。。。。TODO >SimpleUrlMapping,AbstracUrlHandlerMapping

3. ``org.springframework.web.util.UrlPathHelper``

   路径匹配帮助类：协助路径匹配，URL解码。

4. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcContentNegotiationManager``

   ``ContentNegotiationManager`` 判断请求媒体类型的核心类，具体完成过程代理给其持有的内容协商实例``ContentNegotiationStrategy``列表

5. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#viewControllerHandlerMapping``

   返回视图控制器的HandlerMapping  EmptyHandlerMapping 直接返回视图名称，通过#addViewControllers 植入自定义视图控制器。可以匹配指定的路径和指定视图。

6. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#beanNameHandlerMapping``

   ``BeanNameUrlHandlerMapping`` 匹配URL和以"/"开头的bean，

7. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#resourceHandlerMapping`` 

   ``SimpleUrlHandlerMapping`` 携带资源路径格式和处理器的隐射。

8. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcResourceUrlProvider``

   ``ResourceUrlProvider``  提供资源路径格式和处理器的隐射。

9. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#defaultServletHandlerMapping``

   ```tiki wiki
   映射："/**"  --->  DefaultServletHttpRequestHandler 
   ```

   默认Servlet处理器。

10. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#requestMappingHandlerAdapter``

   ``RequestMappingHandlerAdapter``  处理`@RequestMapping` 注解的方法`HandlerMethod` 。支持自定义方法处理和返回值处理，通过调用方法setCustomArgumentResolvers，setCustomReturnValueHandlers。也可完全覆盖默认处理setArgumentResolvers（默认：HandlerMethodArgumentResolverComposite），

   setReturnValueHandlers（默认：HandlerMethodReturnValueHandlerComposite）

   默认参数处理器：

   org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#getDefaultArgumentResolvers

   

11. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcConversionService``

    每个请求所感知的格式化转换服务类

12. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcValidator``

    mvc校验器

13. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcUriComponentsContributor``

    ``CompositeUriComponentsContributor`` 持有``UriComponentsContributor`` 列表，

    ```
    UriComponentsContributor：Strategy for contributing to the building of a {@link UriComponents} by looking at a method parameter and an argument value and deciding what part of the target URL should be updated.
    ```

    ```java
    Collections{
        ...
        public static <T> boolean addAll(Collection<? super T> c, T... elements) {
            boolean result = false;
            for (T element : elements)
                result |= c.add(element);
            return result;
        }
        ...
    }
    ```

14. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#httpRequestHandlerAdapter``

    返回``HttpRequestHandlerAdapter`` 用于使用``HttpRequestHandler``处理请求 。返回最后更新时间，处理请求返回视图模型对象。

15. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#simpleControllerHandlerAdapter``

    适配器用于处理实现Controller接口的API

16. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#handlerExceptionResolver``

    聚合对象``HandlerExceptionResolverComposite``  持有处理器异常处理对象``HandlerExceptionResolver`` 列表。 ``HandlerExceptionResolver`` 处理注解了``@ExceptionHandler`` 的方法。同时根据系统以来的项目决定某些处理器的存在性。

    ```java
    protected final void addDefaultHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
    		ExceptionHandlerExceptionResolver exceptionHandlerResolver = createExceptionHandlerExceptionResolver();
    		exceptionHandlerResolver.setContentNegotiationManager(mvcContentNegotiationManager());
    		exceptionHandlerResolver.setMessageConverters(getMessageConverters());
    		exceptionHandlerResolver.setCustomArgumentResolvers(getArgumentResolvers());
    		exceptionHandlerResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
    		if (jackson2Present) {
    			exceptionHandlerResolver.setResponseBodyAdvice(
    					Collections.singletonList(new JsonViewResponseBodyAdvice()));
    		}
    		if (this.applicationContext != null) {
    			exceptionHandlerResolver.setApplicationContext(this.applicationContext);
    		}
    		exceptionHandlerResolver.afterPropertiesSet();
    		exceptionResolvers.add(exceptionHandlerResolver);
    
    		ResponseStatusExceptionResolver responseStatusResolver = new ResponseStatusExceptionResolver();
    		responseStatusResolver.setMessageSource(this.applicationContext);
    		exceptionResolvers.add(responseStatusResolver);
    
    		exceptionResolvers.add(new DefaultHandlerExceptionResolver());
    	}
    ```

17. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcViewResolver``

    视图 处理器用于处理返回的视图。``ViewResolverComposite`` 聚合对象持有视图处理对象``ViewResolver``列表

18. ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcHandlerMappingIntrospector``

    ``HandlerMappingIntrospector`` 为 ``HandlerMapping`` 的截取器 持有 ``HandlerMapping``列表信息，从``HandlerMapping``中获取信息服务于特定的请求例如跨域请求。

##### ``HttpMessageConverter``

- ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#addDefaultHttpMessageConverters``

- ``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#extendMessageConverters`` 扩展

- converters实际调用的地方：

  org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor#writeWithMessageConverters(T,org.springframework.core.MethodParameter,org.springframework.http.server.ServletServerHttpRequest, org.springframework.http.server.ServletServerHttpResponse)

##### 媒体类型

handlerAdapters

[org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter@6ffa7f8c, org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter@5d9aa1f1, org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter@93559df]

handlerMappings

[org.springframework.web.servlet.handler.SimpleUrlHandlerMapping, springfox.documentation.spring.web.PropertySourcedRequestMappingHandlerMapping, org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping, org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping, org.springframework.web.servlet.handler.SimpleUrlHandlerMapping, org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport-EmptyHandlerMapping, org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport-EmptyHandlerMapping, org.springframework.boot.autoconfigure.web.servlet.WelcomePageHandlerMapping]





DispatcherServlet 中的处理器适配器是初始化
private List<HandlerAdapter> handlerAdapters;
HandlerExecutionChain
RequestMappingHandlerAdapter 适配的时候对请求参数和返回值不做处理另由其他对象处理
HandlerMethodArgumentResolver 处理参数
HandlerMethodReturnValueHandler 处理返回值
DefaultParameterNameDiscoverer 参数名处理器
参数处理集合类
org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.initBinderArgumentResolvers
HandlerMethodReturnValueHandlerComposite

private CallableProcessingInterceptor[] callableInterceptors = new CallableProcessingInterceptor[0];
private AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("MvcAsync");
private DeferredResultProcessingInterceptor[] deferredResultInterceptors = new DeferredResultProcessingInterceptor[0];
private List<HandlerExceptionResolver> handlerExceptionResolvers;