独立应用

嵌入式容器

固化"starter"依赖，简化构建配置

spring和第三方库自动装配

提供运维特性，指标信息、健康检查及外部化配置

注解配置去xml配置





``AnnotationConfigServletWebServerApplicationContext``

`ServletWebServerApplicationContext`



`org.springframework.context.support.AbstractApplicationContext.ApplicationListenerDetector#postProcessMergedBeanDefinition`

收集单例的`ApplicationListener`



``org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor``

处理字段，方法属性注入。



`org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor#postProcessMergedBeanDefinition`

处理bean的初始化方法和销毁方法的调用