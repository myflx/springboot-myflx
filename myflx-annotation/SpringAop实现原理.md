## spring-aop实现原理

> aop是面向切面编程。spring-aop是springframework的一个模块是spring针对aop编程的模块实现。spring-aop模块中具体实现aop联盟，aspectj，为了避免包冲突进行了repackage。



### spring-aop的驱动

​		spring基于xml编程的时候，xml中aop schema会在spring-aop模块META-INF/spring.handers中找到对应的AopNameHandler会为对应的标签注册对应的beandefinition的解析的类。Parser将xml中的bean定义解析出来，然后将定义注册到上下文BeanFactory中。

​		spring注解编程模型中比较出名的Enable模式。Enable模式中spring-aop功能的启动依赖于注解 `EnableAspectJAutoProxy`  这个注解来源spring-context，3.0版本才有。

​		spring-boot中通过固化依赖和spring的spi机制走向自动驱动。spring-boot关于aop的支持是`AopAutoConfiguration`  ，来源于spring-boot-autoconfigure在spring-factories配置中是`EnableAutoConfiguration` 的基于注解的实现类。`AopAutoConfiguration`中会导入`EnabeAspectJProxy`注解驱动同时可以aop配置具体代理方式，默认CGLib。所以spring-aop的的驱动原理还是回归到驱动注解`EnabeAspectJProxy`。

### EnabeAspectJProxy原理

配置类`EnableAspectJProxy`会导入一个bean的注册类

`org.springframework.context.annotation.AspectJAutoProxyRegistrar`

该类继承了`org.springframework.context.annotation.ImportBeanDefinitionRegistrar`这个类对于注解`@Import`定义的实现。

跟踪代码会发现最终通过工具类`org.springframework.aop.config.AopConfigUtils#registerOrEscalateApcAsRequired`注册了一个bean定义

`org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator`



仔细看该类的继承结构他实现了`InstantiationAwareBeanPostProcessor`,而他实现了``BeanPostProcessor`` ， `BeanPostProcessor`会在AbtractApplicationContext#refresh()的中调用后置处理器，所以要重点关注AopProxyCreater后置处理器的实现。

后置处理器中会遍历ClassLoader中的标记了Advice，aop相关的注解并将原始的类进行动态代理最终实现了实际的功能。



#### AnnotationAwareAspectJAutoProxyCreator

在搞清该AOP原理之前需要先梳理清楚[spring-bean的生命周期](./SpringBean生命周期.md)。查看实现我们可以得知我们需要关注两个点：

``org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessBeforeInstantiation``

``org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessAfterInitialization``

##### 实例化之前

如果存在targetClass会直接对他进行代理。

```java
@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		Object cacheKey = getCacheKey(beanClass, beanName);

		if (!StringUtils.hasLength(beanName) || !this.targetSourcedBeans.contains(beanName)) {
			if (this.advisedBeans.containsKey(cacheKey)) {
				return null;
			}
			if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
				this.advisedBeans.put(cacheKey, Boolean.FALSE);
				return null;
			}
		}

		// Create proxy here if we have a custom TargetSource.
		// Suppresses unnecessary default instantiation of the target bean:
		// The TargetSource will handle target instances in a custom fashion.
		TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
		if (targetSource != null) {
			if (StringUtils.hasLength(beanName)) {
				this.targetSourcedBeans.add(beanName);
			}
			Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
			Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
			this.proxyTypes.put(cacheKey, proxy.getClass());
			return proxy;
		}

		return null;
	}
```



##### 初始化之后

```java
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) throws BeansException {
    if (bean != null) {
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        if (!this.earlyProxyReferences.contains(cacheKey)) {
            return wrapIfNecessary(bean, beanName, cacheKey);
        }
    }
    return bean;
}

protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
    if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
        return bean;
    }
    if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
        return bean;
    }
    if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    // Create proxy if we have advice.
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
    if (specificInterceptors != DO_NOT_PROXY) {
        this.advisedBeans.put(cacheKey, Boolean.TRUE);
        Object proxy = createProxy(
            bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
        this.proxyTypes.put(cacheKey, proxy.getClass());
        return proxy;
    }

    this.advisedBeans.put(cacheKey, Boolean.FALSE);
    return bean;
}
```

##### 总结

所以spring-aop的原理主要是结合通过注入bean后置器在bean创建的过程中进行切面逻辑的植入。





























