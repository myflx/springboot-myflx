#### 基于“注解驱动”实现@Enable模块

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SayHelloConfiguration.class)
public @interface EnableSayHello {
}
```

```java

@Configuration
public class SayHelloConfiguration {
    @Bean
    public String sayHello(){
        return "hello";
    }
}
```

```java
@Configuration
@EnableSayHello
public class AnnotationBootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationBootstrap.class);
        applicationContext.refresh();
        String sayHello = applicationContext.getBean("sayHello", String.class);
        System.out.println(sayHello);
        applicationContext.close();
    }
}
```

>仅依赖：spring-context：3.0以后即可。参考@EnableWebMvc的实现。

#### 基于“接口编程”实现@Enable模块

 ```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CachingConfigurationSelector.class)
public @interface EnableCaching {
	boolean proxyTargetClass() default false;
	AdviceMode mode() default AdviceMode.PROXY;
	int order() default Ordered.LOWEST_PRECEDENCE;
}
 ```

```java
public class CachingConfigurationSelector extends AdviceModeImportSelector<EnableCaching> {
    ...
    @Override
	public String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
			case PROXY:
				return getProxyImports();
			case ASPECTJ:
				return getAspectJImports();
			default:
				return null;
		}
	}
    ...
}
```

```java
public abstract class AdviceModeImportSelector<A extends Annotation> implements ImportSelector {
	...
    @Override
	public final String[] selectImports(AnnotationMetadata importingClassMetadata) {
		Class<?> annType = GenericTypeResolver.resolveTypeArgument(getClass(), AdviceModeImportSelector.class);
		Assert.state(annType != null, "Unresolvable type argument for AdviceModeImportSelector");
		AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(importingClassMetadata, annType);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format(
				"@%s is not present on importing class '%s' as expected",
				annType.getSimpleName(), importingClassMetadata.getClassName()));
		}
		AdviceMode adviceMode = attributes.getEnum(this.getAdviceModeAttributeName());
		String[] imports = selectImports(adviceMode);
		if (imports == null) {
			throw new IllegalArgumentException(String.format("Unknown AdviceMode: '%s'", adviceMode));
		}
		return imports;
	}
    ...
}
```

- ``org.springframework.core.ResolvableType``:将原生java Class 封装便于获取父类，泛型类型，接口等。

可以通过Filed,Class,方法参数，方法返回类型。

- ``org.springframework.core.GenericTypeResolver``: 处理泛型的帮助类。获取泛型。

- ``org.springframework.context.annotation.AnnotationConfigUtils``:注解配置工具类。方便注册基于注解配置的`BeanPostProcessor`，`BeanFactoryPostProcessor`。同时注册普通`AutowireCandidateResolver`。
- ``org.springframework.aop.config.AopConfigUtils` `:工具类用于处理面向切面的自动代理创建器的注册。
- 使用注解@EnableCaching  默认切面类型：PROXY 会导入以下三个配置类
  - `org.springframework.context.annotation.AutoProxyRegistrar` ：基于`BeanDefinitionRegistry`的代理创建对象，对于存在mode(AdviceMode.class)，proxyTargetClass（Boolean.class）的@EnableXxx 的注解进行适当的处理。
  - ``org.springframework.cache.annotation.ProxyCachingConfiguration``:配置类用于注册必要的Spring基础Bean来激活基于代理的注解启动的缓存管理 器。
  - ``org.springframework.cache.jcache.config.ProxyJCacheConfiguration``:JSR107依赖存在时候启动的配置。
- 使用注解@EnableCaching  默认切面类型：ASPECTJ会导入以下配置类
  - ``org.springframework.cache.aspectj.AspectJCachingConfiguration`` :TODO，以及兼容jsr107配置类

- ``org.springframework.context.annotation.ImportAware`` :感知注解驱动的元数据。

