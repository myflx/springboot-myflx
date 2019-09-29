#### Spring，Spring-boot注解配置类的加载

##### Spring

###### 定位

spring 框架中加载配置类主要通过xml配置，<annotation-config/> 加载，解析xml时通过xml中NameSpace 找到对应的Handler，可以通过springframework-context.spring.handlers 找到对应的处理器为:

``org.springframework.context.config.ContextNamespaceHandler``

```java
public class ContextNamespaceHandler extends NamespaceHandlerSupport {
	@Override
	public void init() {
		registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
		registerBeanDefinitionParser("property-override", new PropertyOverrideBeanDefinitionParser());
		registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
		registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
		registerBeanDefinitionParser("load-time-weaver", new LoadTimeWeaverBeanDefinitionParser());
		registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-export", new MBeanExportBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-server", new MBeanServerBeanDefinitionParser());
	}
}
```

###### 注册定义后置处理

所以可以sprig框架中解析配置类的对象是``AnnotationConfigBeanDefinitionParser`` ,下面继续跟踪代码。

```java
public class AnnotationConfigBeanDefinitionParser implements BeanDefinitionParser {
	@Override
	@Nullable
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		Object source = parserContext.extractSource(element);
		// Obtain bean definitions for all relevant BeanPostProcessors.
		Set<BeanDefinitionHolder> processorDefinitions =		AnnotationConfigUtils.registerAnnotationConfigProcessors(parserContext.getRegistry(), source);
		// Register component for the surrounding <context:annotation-config> element.
		CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
		parserContext.pushContainingComponent(compDefinition);
		// Nest the concrete beans in the surrounding component.
		for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
			parserContext.registerComponent(new BeanComponentDefinition(processorDefinition));
		}
		// Finally register the composite component.
		parserContext.popAndRegisterContainingComponent();
		return null;
	}
}
```

所以处理核心是:

```java
Set<BeanDefinitionHolder> processorDefinitions =		AnnotationConfigUtils.registerAnnotationConfigProcessors(parserContext.getRegistry(), source);
```

跟踪代码发现注册的处理器是：

``org.springframework.context.annotation.ConfigurationClassPostProcessor`` 他继承了接口``PriorityOrdered``优先级是最高的，做实际处理工作的接口是：``BeanDefinitionRegistryPostProcessor``

``BeanFactoryPostProcessor``  

###### 处理器调用

接下来就是后置处理器如何将项目中配置的配置类加载进项目中，查看上下文启动方法

``AbstractApplicationContext.refresh()``中的实际操作：

```java
public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);
			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);
				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);
				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);
				// Initialize message source for this context.
				initMessageSource();
				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();
				// Initialize other special beans in specific context subclasses.
				onRefresh();
				// Check for listener beans and register them.
				registerListeners();
				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);
				// Last step: publish corresponding event.
				finishRefresh();
			}
			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +"cancelling refresh attempt: " + ex);
				}
				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();
				// Reset 'active' flag.
				cancelRefresh(ex);
				// Propagate exception to caller.
				throw ex;
			}
			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}
```

``invokeBeanFactoryPostProcessors(beanFactory);`` 即为实际解析点。会调用两个关键接口唯一方法，

``BeanDefinitionRegistryPostProcessor``，``BeanFactoryPostProcessor``  

```java
registryProcessor.postProcessBeanDefinitionRegistry(registry);
invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
	->invokeBeanFactoryPostProcessors(registry);
```

继续跟踪Bean定义注册的后置处理，可以看到核心解析类 :

###### 配置类解析

``org.springframework.context.annotation.ConfigurationClassParser#parse(java.util.Set<org.springframework.beans.factory.config.BeanDefinitionHolder>)`` 对配置类进行解析，``@PropertySources`` ,``@ComponentScan``  ,``@Import`` ,``@ImportResourceImportResource``  ,``@Bean``  （支持接口default方法的@Bean）,     ``@ImportResource``，同时支持对接口编程类型的对象到处进行处理：``@ImportSelector`` ，``@ImportBeanDefinitionRegistrar``。  ``ConditionEvaluator`` 对``@Conditional`` 注解的类逻辑判断。循环处理，直到内建注解 。完成对配置类的收集。

###### 配置类注册

``org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader#loadBeanDefinitions``     内部已解析的配置进行注册。``TrackedConditionEvaluator`` 深入判断是否需要将Bean 注册。对不同类型的类进行特定的注册。

###### 工厂后置处理调用

定义后置调用之后紧接着会调用工厂的后置处理，可以查看refresh()->invokeBeanFactoryPostProcessors代码后半部分。查看ConfigurationClassPostProcessor 中相关方法。。

```java
@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		int factoryId = System.identityHashCode(beanFactory);
		if (this.factoriesPostProcessed.contains(factoryId)) {
			throw new IllegalStateException(
					"postProcessBeanFactory already called on this post-processor against " + beanFactory);
		}
		this.factoriesPostProcessed.add(factoryId);
		if (!this.registriesPostProcessed.contains(factoryId)) {
			// BeanDefinitionRegistryPostProcessor hook apparently not supported...
			// Simply call processConfigurationClasses lazily at this point then.
			processConfigBeanDefinitions((BeanDefinitionRegistry) beanFactory);
		}

		enhanceConfigurationClasses(beanFactory);
		beanFactory.addBeanPostProcessor(new ImportAwareBeanPostProcessor(beanFactory));
	}
```

- 如果定义后置回调没有被调用就再次调用。
- 增强配置处理类。enhanceConfigurationClasses。
- 注册后置处理器：``ImportAwareBeanPostProcessor`` 为继承接口``ImportAware`` 回调注入注解元数据。

###### 配置类的重量级

spring 将配置类分为全量级别和轻量级别。通过下面方法判断：

``ConfigurationClassUtils.isFullConfigurationClass(beanDef)`` 如果定义全量级属性为full则进行增强。

那么那些配置为增强对象？

在后置定义处理器处理过程中，解析配置类之前有一段代码：

```java
ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)
```

```java
---->
if (isFullConfigurationCandidate(metadata)) {
    beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_FULL);
}
---->
public static boolean isFullConfigurationCandidate(AnnotationMetadata metadata) {
    return metadata.isAnnotated(Configuration.class.getName());
}
```

所以得到结论：spring 配置类分为全量和轻量级对象。注解了``@Configuration`` 的类为全量级，其他大部分为轻量级对象。

增强帮助类为：``ConfigurationClassEnhancer`` 实际增强如下：

```java
/**
 * Creates a new CGLIB {@link Enhancer} instance.
 */
private Enhancer newEnhancer(Class<?> configSuperClass, @Nullable ClassLoader classLoader) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(configSuperClass);
    enhancer.setInterfaces(new Class<?>[] {EnhancedConfiguration.class});
    enhancer.setUseFactory(false);
    enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
    enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader));
    enhancer.setCallbackFilter(CALLBACK_FILTER);
    enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
    return enhancer;
}
```





##### Spring-boot

###### 注册定义后置处理

spring-boot 对配置类的加载底层是依赖spring的，启动类启动的上下文不同，所有后置处理器的注册是在spring-boot的某一个上下文中。通过查看相关上下文和方法调用可以定位到，后置处理器的注册点。

``AnnotationConfigUtils.registerAnnotationConfigProcessors``

- spring-boot上下文：

  ``AnnotationConfigServletWebServerApplicationContext``

- 独立上下文：

  ``AnnotationConfigApplicationContext/ ClassPathXmlApplicationContext``

其中sb上下文和独立注解上下文均以同样的方式持有：注解类型的bean定义读取对象``AnnotatedBeanDefinitionReader``

其构造方法对后置处理器进行注册：

```java
public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		Assert.notNull(environment, "Environment must not be null");
		this.registry = registry;
		this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);
		AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
	}
```

对后置处理器的调用依赖的就是spring了。