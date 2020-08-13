## spring-aop实现原理

> aop是面向切面编程。spring-aop是springframework的一个模块是spring针对aop编程的模块实现。spring-aop模块中具体实现aop联盟，aspectj，为了避免包冲突进行了repackage。



### spring-aop的驱动

​		spring基于xml编程的时候，xml中aop schema会在spring-aop模块META-INF/spring.handers中找到对应的AopNameHandler会为对应的标签注册对应的beandefinition的解析的类。Parser将xml中的bean定义解析出来，然后将定义注册到上下文。

​		spring注解编程模型中比较出名的Enable模式。Enable模式中spring-aop功能的启动依赖于注解 `EnableAspectJAutoProxy`  这个注解来源spring-context，3.0版本才有。

​		spring-boot中通过固化依赖和spring的spi机制走向自动驱动。spring-boot关于aop的支持是`AopAutoConfiguration`  ，来源于spring-boot-autoconfigure在spring-factories配置中是`EnableAutoConfiguration` 的基于注解的实现类。`AopAutoConfiguration`中会导入`EnabeAspectJProxy`注解驱动同时可以aop配置具体代理方式，默认CGLib。所以spring-aop的的驱动原理还是回归到驱动注解`EnabeAspectJProxy`。

### EnabeAspectJProxy原理

配置类`EnableAspectJProxy`会导入一个bean的注册类

`org.springframework.context.annotation.AspectJAutoProxyRegistrar`

该类继承了`org.springframework.context.annotation.ImportBeanDefinitionRegistrar`这个类对于注解`@Import`定义的实现。

跟踪代码会发现最终通过工具类`org.springframework.aop.config.AopConfigUtils#registerOrEscalateApcAsRequired`注册了一个bean定义

`org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator`



仔细看该类的继承结构他是一个``BeanPostProcessor`` ， `BeanPostProcessor`会在AbtractApplicationContext#refresh()的中调用后置处理器，所以要重点关注AopProxyCreater后置处理器的实现。

后置处理器中会遍历ClassLoader中的标记了Advice，aop相关的注解并将原始的类进行动态代理最终实现了实际的功能。