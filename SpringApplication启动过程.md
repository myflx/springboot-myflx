## SpringApplication

### 1.启动方式

#### 1.1 SpringApplication

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

#### 1.2 SpringApplicationBuilder

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        final SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(DemoApplication.class);
        springApplicationBuilder.properties("server.port:9090");
        springApplicationBuilder.run(args);
    }
}
```

> SpringApplicationBuilder提供fluentAPI可以添加properties,environment,listener,contextInitializer等等

### 2.SpringApplication构建

```java
@SuppressWarnings({ "unchecked", "rawtypes" })
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    this.resourceLoader = resourceLoader;
    Assert.notNull(primarySources, "PrimarySources must not be null");
    this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
    this.webApplicationType = deduceWebApplicationType();
    setInitializers((Collection) getSpringFactoriesInstances(
        ApplicationContextInitializer.class));
    setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
    this.mainApplicationClass = deduceMainApplicationClass();
}
```

#### 2.2 primarySources/mainApplicationClass

​		primarySources为注解了启动注解的类，注意他不一定是main方法所在的类。实际项目可以将启动配置类和主方法类分开，配置类统一管理且不对外开放。程序的主类入口是通过调用栈判断出来的。

```java
private Class<?> deduceMainApplicationClass() {
    try {
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                return Class.forName(stackTraceElement.getClassName());
            }
        }
    }
    catch (ClassNotFoundException ex) {
        // Swallow and continue
    }
    return null;
}
```



#### 2.3 ResourceLoader

​		``ResourceLoader`` 是用来加载或者系统文件资源的策略接口，上下文 ``org.springframework.context.ApplicationContext`` 必须提供改功能，同时要继承 ``org.springframework.core.io.support.ResourcePatternResolver``  ``DefaultResourceLoader`` 是他的独立实现，可被外部上下文以及 ``ResourceEditor``使用。其或其数组也通常作为属性填充进bean中。``org.springframework.context.ResourceLoaderAware`` 可提供``ResourceLoader`` 的回调注入。

​		通过**SpringApplication**的方式启动是没有资源加载器的，上下文本身就是资源加载器。通过**SpringApplicationBuilder**启动的时候可以自定义资源加载器。

#### 2.4 类型推断

​	SpringBootApplication启动的时候会根据系统的依赖来推断当前的应用类型

##### 2.4.1 应用类型

```java
public enum WebApplicationType {
	/**
	 * The application should not run as a web application and should not start an
	 * embedded web server.
	 * 相当于启动普通的main方法
	 */
	NONE,
	/**
	 * The application should run as a servlet-based web application and should start an
	 * embedded servlet web server.
	 * 启动嵌入式的servlet服务:tomcat，jetty,undertow
	 */
	SERVLET,
	/**
	 * The application should run as a reactive web application and should start an
	 * embedded reactive web server.
	 * 启动嵌入式reactive web服务，除了要依赖spring-boot-webflux外还要基于其他的web server,
	 * webfulx 相当于spring自定义的一套Servlet规范，从源代码上看很多都是copy过去改改名字。
	 */
	REACTIVE
}
```

##### 2.4.2 推断过程

``org.springframework.boot.SpringApplication#deduceWebApplicationType``

```java
private static final String REACTIVE_WEB_ENVIRONMENT_CLASS = "org.springframework."
			+ "web.reactive.DispatcherHandler";
private static final String MVC_WEB_ENVIRONMENT_CLASS = "org.springframework."
			+ "web.servlet.DispatcherServlet";
private static final String[] WEB_ENVIRONMENT_CLASSES = { "javax.servlet.Servlet",
			"org.springframework.web.context.ConfigurableWebApplicationContext" };
private WebApplicationType deduceWebApplicationType() {
		if (ClassUtils.isPresent(REACTIVE_WEB_ENVIRONMENT_CLASS, null)
				&& !ClassUtils.isPresent(MVC_WEB_ENVIRONMENT_CLASS, null)) {
			return WebApplicationType.REACTIVE;
		}
		for (String className : WEB_ENVIRONMENT_CLASSES) {
			if (!ClassUtils.isPresent(className, null)) {
				return WebApplicationType.NONE;
			}
		}
		return WebApplicationType.SERVLET;
	}
```

- 当``org.springframework.web.reactive.DispatcherHandler``存在即依赖了spring-webflux，同时没有依赖``DispatcherServlet``即没有依赖spring-webmvc。才认定当前应用类型为REACTIVE。
- 既未依赖servelet-pi 又未依赖spring-web 就断定为普通应用（仅仅运行main方法）。
-  其他场景默认应用为Servlet server环境。



#### 2.5 ApplicationContextInitializer

​		`ApplicationContextInitializer`  是上下文`ConfigurableApplicationContext#refresh()` 刷新之前调用的回调接口。通常用于web application的必要初始化：registering property sources 、activating profiles（针对ConfigurableApplicationContext#getEnvironment() 上下文中的environments） 。spring中要参考``ContextLoader`` ，``FrameworkServlet`` 中如何各自获取并调用回调。

​		通过``org.springframework.core.io.support.SpringFactoriesLoader `` 获取spring-boot-*.jar/META-INF/spring.factories中配置的应用上下文初始化器并进行排序。

```properties
# Application Context Initializers
org.springframework.context.ApplicationContextInitializer=\
org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer,\
org.springframework.boot.context.ContextIdApplicationContextInitializer,\
org.springframework.boot.context.config.DelegatingApplicationContextInitializer,\
org.springframework.boot.web.context.ServerPortInfoApplicationContextInitializer
```

#### 2.6 ApplicationListener

​		同样配置在spring.factories文件里边,如下。

```properties
org.springframework.context.ApplicationListener=\
org.springframework.boot.ClearCachesApplicationListener,\
org.springframework.boot.builder.ParentContextCloserApplicationListener,\
org.springframework.boot.context.FileEncodingApplicationListener,\
org.springframework.boot.context.config.AnsiOutputApplicationListener,\
org.springframework.boot.context.config.ConfigFileApplicationListener,\
org.springframework.boot.context.config.DelegatingApplicationListener,\
org.springframework.boot.context.logging.ClasspathLoggingApplicationListener,\
org.springframework.boot.context.logging.LoggingApplicationListener,\
org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener
```

> ApplicationContextInitializer和ApplicationListener的回调均在上下文启动中有调用，参见下文。

### 3.上下文及环境

​		针对不同应用类型加载不同的上下文 ``org.springframework.context.ApplicationContext`` 以及系统环境``org.springframework.core.env.Environment``，下面对不同应用类型进行汇总。

#### 3.1 分类

| 应用类型 |                       上下文                        |          系统环境          |
| :------: | :-------------------------------------------------: | :------------------------: |
|   NONE   |         AnnotationConfigApplicationContext          |    StandardEnvironment     |
| SERVLET  | AnnotationConfigServletWebServerApplicationContext  | StandardServletEnvironment |
| REACTIVE | AnnotationConfigReactiveWebServerApplicationContext |    StandardEnvironment     |



### 4.运行

```java
public ConfigurableApplicationContext run(String... args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ConfigurableApplicationContext context = null;
		Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
		configureHeadlessProperty();
		SpringApplicationRunListeners listeners = getRunListeners(args);
		listeners.starting();
		try {
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(
					args);
			ConfigurableEnvironment environment = prepareEnvironment(listeners,
					applicationArguments);
			configureIgnoreBeanInfo(environment);
			Banner printedBanner = printBanner(environment);
			context = createApplicationContext();
			exceptionReporters = getSpringFactoriesInstances(
					SpringBootExceptionReporter.class,
					new Class[] { ConfigurableApplicationContext.class }, context);
			prepareContext(context, environment, listeners, applicationArguments,
					printedBanner);
			refreshContext(context);
			afterRefresh(context, applicationArguments);
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass)
						.logStarted(getApplicationLog(), stopWatch);
			}
			listeners.started(context);
			callRunners(context, applicationArguments);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, listeners);
			throw new IllegalStateException(ex);
		}

		try {
			listeners.running(context);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, null);
			throw new IllegalStateException(ex);
		}
		return context;
	}
```

#### 4.1 StopWatch

​		用于记录某个任务执行持续时长，在`SpringApplication` 运行程序的过程中记录整个应用启动的时长。同时记录任务ID，名称，提供日志信息的打印方法。在一些方法拦截器对象中也有使用。

``org.springframework.aop.interceptor.PerformanceMonitorInterceptor`` ``org.springframework.aop.interceptor.CustomizableTraceInterceptor``

#### 4.2 headless 模式

​	Headless模式是系统的一种配置模式。在该模式下，系统缺少了显示设备、键盘或鼠标。spring-boot默认将系统设置在该模式下。

```java
private static final String SYSTEM_PROPERTY_JAVA_AWT_HEADLESS = "java.awt.headless";
private void configureHeadlessProperty() {
    System.setProperty(SYSTEM_PROPERTY_JAVA_AWT_HEADLESS, System.getProperty(
        SYSTEM_PROPERTY_JAVA_AWT_HEADLESS, Boolean.toString(this.headless)));
}
```

#### 4.3 SpringApplicationRunListeners

``SpringApplicationRunListeners`` 是应用运行的监听器``SpringApplicationRunListener`` 的聚合对象，也是对其方法调用的封装，也是从spring.factories中获取。

```properties
org.springframework.boot.SpringApplicationRunListener=\
		org.springframework.boot.context.event.EventPublishingRunListener
```

``EventPublishingRunListener`` 初始化时也会初始化 事件发布器 ``SimpleApplicationEventMulticaster`` 并持有**2.6** 所说的 ``ApplicationListener``  。在调用时将事件类型和事件源作为key缓存在 ``org.springframework.context.event.AbstractApplicationEventMulticaster#retrieverCache`` 中。



构建SpringBootApplication对象并运行org.springframework.boot.SpringApplication#run(java.lang.Class<?>, java.lang.String...)
命令行入参被包装成对象：org.springframework.boot.ApplicationArguments（org.springframework.boot.DefaultApplicationArguments）同时被进一步包装成org.springframework.core.env.PropertySource（EnumerablePropertySource,CommandLinePropertySource,SimpleCommandLinePropertySource,Source）
配置获取BeanInfo的方式:默认从Introspector.getBeanInfo(); spring.beaninfo.ignore默认true

构建异常报告（只告诉应用是否有指定的异常，为了保证程序正常启动其他异常是忽略的）：
org.springframework.boot.SpringBootExceptionReporter（org.springframework.boot.diagnostics.FailureAnalyzers）
	持有异常报告分析器：List<FailureAnalyzer> analyzers;
org.springframework.boot.diagnostics.FailureAnalyzer=\
org.springframework.boot.diagnostics.analyzer.BeanCurrentlyInCreationFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.BeanNotOfRequiredTypeFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.BindFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.BindValidationFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.UnboundConfigurationPropertyFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.ConnectorStartFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.NoUniqueBeanDefinitionFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.PortInUseFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.ValidationExceptionFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.InvalidConfigurationPropertyNameFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.InvalidConfigurationPropertyValueFailureAnalyzer
主线程绑定异常处理器：org.springframework.boot.SpringBootExceptionHandler

异常是时处理退出码：org.springframework.boot.SpringApplication#handleExitCode


启动Banner打印：org.springframework.boot.SpringApplicationBannerPrinter.PrintedBanner

启动时上下文准备的操作：org.springframework.boot.SpringApplication#prepareContext
	1，关联环境
	2，上下文后置处理：注册beanNameGenerator，resourceLoader（以SpringBootApplicationBuilder方式设置的时候调用否则是空没有注册）
	3，启动前应用上下文初始器（ ApplicationContextInitializer ） 

		# Application Context Initializers

​	4，通知监听器环境以及准备好org.springframework.boot.SpringApplicationRunListeners#contextPrepared	
​		
​		发布事件（空）
​	5，注册基础单例
​		命令行包装对象(ApplicationArguments)注册为spring bean:springApplicationArguments	
​		注册banner对象：springBootBanner
​	6，加载资源
​		获取启动类数组
​		创建org.springframework.boot.BeanDefinitionLoader 加载资源org.springframework.boot.BeanDefinitionLoader#load(java.lang.Class<?>)。
​		调用org.springframework.context.annotation.AnnotatedBeanDefinitionReader#register(Class<?>... annotatedClasses) 注册所有注解驱动的资源。
​		实际注册操作是在org.springframework.context.annotation.AnnotatedBeanDefinitionReader#doRegisterBean里边
​	7，通知监听器上下文资源以及加载：org.springframework.boot.SpringApplicationRunListeners#contextLoaded
​		发布事件：ApplicationPreparedEvent

开始启动上下文：
	启动上下文
		启动上下文中的监听器来源spring.factories

		# Application Listeners
​		
​		

```java
	嵌入式容器启动点在上下文refresh()中org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#onRefresh 实现，其中创建WebServer之后会回调容器中的ServletContextInitializer对象，实现点在：org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#createWebServer#getSelfInitializer().onStartup(servletContext);
	DipatcherServlet的加载来源于：org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration.DispatcherServletRegistrationConfiguration#dispatcherServletRegistration
	同时属性资源的加载也在创建WebServer的中方法#initPropertySources();
注册钩子（org.springframework.context.support.AbstractApplicationContext#registerShutdownHook） 主要执行上下文关闭之后的资源回收操作。
```

上下文刷新之后的子类实现后门：org.springframework.boot.SpringApplication#afterRefresh
关闭stopWatch
通知监听器环境已经启动完毕：org.springframework.boot.SpringApplicationRunListeners#started
	发布事件：ApplicationStartedEvent
回调runner：org.springframework.boot.SpringApplication#callRunners
	Runner类型：org.springframework.boot.ApplicationRunner，org.springframework.boot.CommandLineRunner
通知监听器环境运行中:org.springframework.boot.SpringApplicationRunListeners#running
	发布事件：ApplicationReadyEvent	
		
		

​		

其他：
	处理泛型：org.springframework.core.GenericTypeResolver

ResolveType








