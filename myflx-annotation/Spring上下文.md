## Spring上下文

上下文`ApplicationContext` 是spring定义的用于管理环境变量，bean工厂，消息源，事件和加载资源的对象。上下文能持有另外一个上下文对象作为父上下文。父子上下文能隔离资源将bean分区域管理，能在一定程度上保证安全性。

``org.springframework.context.ApplicationContext``

```java
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
```

### springmvc上下文

父容器：spring，springmvc环境中父容器中父容器由`org.springframework.web.context.ContextLoaderListener` 加载，`ContextLoaderListener`实现了`javax.servlet.ServletContextListener`按照servlet规范`ServletContext`加载完成之后会调用所有`ServletContextListener`，Servlet中会将attributes作为servlet级别属性的传递。所以`ServletContextListener`中会将父容器作为一个属性加载在servlet上下文中，`WebApplicationContext.ROOT`作为key值。

子容器：springmvc也是servlet编程的实现，所以web.xml中配置的servlet会按照生命周期init()，service()，destroy()流转。spring中的核心是`DispatcherServlet`，spring会为他创建一个单独的上下文。

他继承了`org.springframework.web.servlet.FrameworkServlet`，`org.springframework.web.servlet.HttpServletBean`，`javax.servlet.http.HttpServlet`。

`HttpServletBean`实现了``javax.servlet.Servlet#init()``方法。

其子类`org.springframework.web.servlet.FrameworkServlet#initWebApplicationContext`负责实现创建并启动上下文。

最后调子类：`org.springframework.web.servlet.DispatcherServlet#onRefresh`负责实现`DispatcherServlet`的9大组件。

```java
protected void initStrategies(ApplicationContext context) {
    initMultipartResolver(context);//文本上传处理器
    initLocaleResolver(context);//本地语言处理器
    initThemeResolver(context);//模板处理器
    initHandlerMappings(context);//初始化HandlerMapping用于映射Hanler
    initHandlerAdapters(context);//初始化Handler的参数适配器
    initHandlerExceptionResolvers(context);//初始化异常拦截器
    initRequestToViewNameTranslator(context);//初始化视图预处理器
    initViewResolvers(context);//初始化视图转换器
    initFlashMapManager(context);
}
```



### springcloud上下文

springcloud上下文主要分为三块：BootstrapApplicationContext，Springboot级别容器，NamedContextFactory

#### Bootstrap上下文



#### SpringBoot上下文



#### 微服务配置容器





[父子容器参考](https://blog.csdn.net/forezp/article/details/87910226)