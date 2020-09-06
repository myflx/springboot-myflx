#### ComponentScan加载原理

​	spring可扩展xml编写机制规定元素XML Schema命名空间需要与其处理类建立隐射关系，隐射资源文件约定放置在``META-INF\spring.handlers`` 文件中：

例：spring-context jar 包（spring-context-2.5.6.SEC03.jar!\META-INF\spring.handlers）：

```properties
http\://www.springframework.org/schema/context=org.springframework.context.config.ContextNamespaceHandler
http\://www.springframework.org/schema/jee=org.springframework.ejb.config.JeeNamespaceHandler
http\://www.springframework.org/schema/lang=org.springframework.scripting.config.LangNamespaceHandler
```

所有context标签的处理类为: ``org.springframework.context.config.ContextNamespaceHandler``

定义解析类：

``this.registerJava5DependentParser("component-scan", "org.springframework.context.annotation.ComponentScanBeanDefinitionParser");``

``org.springframework.context.annotation.ClassPathBeanDefinitionScanner``

``org.springframework.core.type.classreading.MetadataReader``

```java
public interface MetadataReader {
    ClassMetadata getClassMetadata();

    AnnotationMetadata getAnnotationMetadata();
}
```
MetaData 的实现类是：SimpleMetadataReader
SimpleMetadataReader 实现中：
        通过 org.springframework.core.type.classreading.ClassMetadataReadingVisitor 获取ClassMetaData
        通过 org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor 获取AnnotationMetadata


org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor 为获取注解元数据的最终类
​	ClassPathBeanDefinitionScanner 继承了 ClassPathScanningCandidateComponentProvider 默认调用父类的构造方法注册默认过滤器，
ClassPathScanningCandidateComponentProvider执行实际的扫描工作返回Set<BeanDefinition> ClassPathBeanDefinitionScanner 负责将
定义包装为定义持有者集合Set<BeanDefinitionHolder>，交给后续处理。对比 AnnotationMetadataReadingVisitor 对于多层注解的支持在
spring 4.0.0 得以实现，查询源码，因为其实现了元注解的递归查找。
org.springframework.core.type.classreading.AnnotationAttributesReadingVisitor.recursivelyCollectMetaAnnotations
```java
private void recursivelyCollectMetaAnnotations(Set<String> visited, Annotation annotation) {
        if (visited.add(annotation.annotationType().getName()) && Modifier.isPublic(annotation.annotationType().getModifiers())) {
            this.attributesMap.add(annotation.annotationType().getName(), AnnotationUtils.getAnnotationAttributes(annotation, true, true));
            Annotation[] var3 = annotation.annotationType().getAnnotations();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Annotation metaMetaAnnotation = var3[var5];
                this.recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
            }
        }

    }
```

``org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider``
```java
   // ... 构造时候会创建默认 过滤器 为后续处理Component标记的类做铺垫，运行自定义过滤器来进行处理。。
    protected void registerDefaultFilters() {
		this.includeFilters.add(new AnnotationTypeFilter(Component.class));
	}
```
AnnotationMetadata 两个重要实现：
        ASM:org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor
        Java Reflect:org.springframework.core.type.StandardAnnotationMetadata

思考+提出问题->验证代码+结合javaDoc+wiki

spring自动注入同时支持jsr:330 java.inject.*的注解。