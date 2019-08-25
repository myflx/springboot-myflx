#### spring-webmvc 整合JSR303原理

##### 如何整合校验器？

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

`org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean` 主要是实现了：

`javax.validation.ValidatorFactory`。



##### 调用过程？

`org.springframework.web.servlet.DispatcherServlet#doDispatch`     

然后通过检测出来适配器将请求适配为具体的 ModelAndView，关于bean的校验肯定在适配的中间的某个过程调用了。接着看方法的调用。

`org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#resolveArgument`

该方法主要处理目标方法的餐宿，在里边组装关键对象：`org.springframework.web.bind.WebDataBinder` 主要通过工厂类 `org.springframework.web.bind.support.WebDataBinderFactory` 构造。方法实现类：`org.springframework.web.bind.support.DefaultDataBinderFactory#createBinder`   。

紧接着通过循环初始化时候装载的校验器：``org.springframework.validation.DataBinder#validators``

该容器里边对象为``org.springframework.boot.autoconfigure.validation.ValidatorAdapter`` 

ValidatorAdapter 作为桥梁将持有的对象 

``org.springframework.validation.beanvalidation.LocalValidatorFactoryBean`` 作为代理执行校验，继续代理调用 

``org.springframework.validation.beanvalidation.SpringValidatorAdapter#validate(java.lang.Object, org.springframework.validation.Errors, java.lang.Object...)``   

```java
@Override
public void validate(@Nullable Object target, Errors errors, @Nullable Object...validationHints) {
    if (this.targetValidator != null) {
        Set<Class<?>> groups = new LinkedHashSet<>();
        if (validationHints != null) {
            for (Object hint : validationHints) {
                if (hint instanceof Class) {
                    groups.add((Class<?>) hint);
                }
            }
        }
        processConstraintViolations(
            this.targetValidator.validate(target, ClassUtils.toClassArray(groups)), errors);
    }
}
```

其中 targetValidator 为JSR-303 Validtor 的内部实现：ValidtorImpl。最中执行校验逻辑。经过方法调用：

``org.springframework.validation.beanvalidation.SpringValidatorAdapter#processConstraintViolations``之后，将Set<ConstraintViolation<Object> 转化为 ``BindingResult``其实现类为：

 ``org.springframework.validation.BeanPropertyBindingResult``

校验完毕回到 方法中：

``org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#resolveArgument``

```java
@Override
public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                              NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

    parameter = parameter.nestedIfOptional();
    Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
    String name = Conventions.getVariableNameForParameter(parameter);

    if (binderFactory != null) {
        WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
        if (arg != null) {
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
        if (mavContainer != null) {
            mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        }
    }

    return adaptArgumentIfNecessary(arg, parameter);
}
```

> if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
>      throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
> }

``WebDataBinder``对象持有对请求方法对象参数校验的结果

##### 如何识别校验开关？

在参数处理的方法中存在调用：

``org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver#validateIfApplicable``

```java
protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
    Annotation[] annotations = parameter.getParameterAnnotations();
    for (Annotation ann : annotations) {
        Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
        if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
            Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
            Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
            binder.validate(validationHints);
            break;
        }
    }
}
```

> 一目了然：既支持@Validated 的又兼容所有名字以Valid打头的注解。



##### MethodParameter 如何加载的？



##### 列表 ``org.springframework.validation.DataBinder#validators`` 是如何初始化的？