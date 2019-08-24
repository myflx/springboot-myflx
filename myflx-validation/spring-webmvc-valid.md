---
typora-copy-images-to: pic
---



#### spring-webmvc 整合JSR303原理

首先要从spring-boot mvc驱动注解``@EnableWebMvc`` 说起 ，自动装配对象为``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration``继承了

``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport``对象。想springContext中注入了Validator对象

```java
@Bean
public Validator mvcValidator() {
    Validator validator = getValidator();
    if (validator == null) {
        if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
            Class<?> clazz;
            try {
                String className = "org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean";
                clazz = ClassUtils.forName(className, WebMvcConfigurationSupport.class.getClassLoader());
            }
            catch (ClassNotFoundException | LinkageError ex) {
                throw new BeanInitializationException("Failed to resolve default validator class", ex);
            }
            validator = (Validator) BeanUtils.instantiateClass(clazz);
        }
        else {
            validator = new NoOpValidator();
        }
    }
    return validator;
}
```

那么要重点研究一下这个对象：

`org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean











`org.springframework.web.servlet.DispatcherServlet#doDispatch`

`...`

`org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#resolveArgument`

在该方法里边组装关键对象：`org.springframework.web.bind.WebDataBinder` 主要通过工厂类 `org.springframework.web.bind.support.WebDataBinderFactory` 构造。方法实现类：`org.springframework.web.bind.support.DefaultDataBinderFactory#createBinder`













