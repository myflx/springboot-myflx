#### spring-webmvc 整合JSR303原理

##### 如何整合校验器？

首先从springframe-mvc驱动注解``@EnableWebMvc`` 看起 ，注解自动装配的对象为：

``org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration``继承了

``org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport``对象。向springContext中注入了Validator对象

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

`javax.validation.ValidatorFactory`，`javax.validation.Validator`。。。



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



##### SpringValidatorAdapter.targetValidator中的实现为ValidatorImpl，是如何初始化的？



##### 如何识别校验开关、校验组？

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
>
> 同时取注解的value作为校验组，默认Default.class（可以做什么？）



##### MethodParameter 如何加载的？

​		``org.springframework.core.MethodParameter#getParameterAnnotations`` 主要是从方法参数对象中获取参数注解数组。具体获取的代理对象是：private final Executable executable; 继续查看而这个Executable 对象的实现还是当前的Method。Executable 是对方法（包括构造方法）的抽象，主要是对方法的相关操作。

```java
public HandlerMethod(Object bean, Method method) {
    Assert.notNull(bean, "Bean is required");
    Assert.notNull(method, "Method is required");
    this.bean = bean;
    this.beanFactory = null;
    this.beanType = ClassUtils.getUserClass(bean);
    this.method = method;
    this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
    this.parameters = initMethodParameters();
    evaluateResponseStatus();
}

private MethodParameter[] initMethodParameters() {
    int count = this.bridgedMethod.getParameterCount();
    MethodParameter[] result = new MethodParameter[count];
    for (int i = 0; i < count; i++) {
        HandlerMethodParameter parameter = new HandlerMethodParameter(i);
        GenericTypeResolver.resolveParameterType(parameter, this.beanType);
        result[i] = parameter;
    }
    return result;
}

public HandlerMethodParameter(int index) {
    super(HandlerMethod.this.bridgedMethod, index);
}
```

​		在上面校验开关的中的方法validateIfApplicable 就会调用获取注解方法Annotation[] annotations = parameter.getParameterAnnotations();   注意到其中可执行返回的是二维数组，然后将其解析为一维数组。

```java
public Annotation[] getParameterAnnotations() {
    Annotation[] paramAnns = this.parameterAnnotations;
    if (paramAnns == null) {
        Annotation[][] annotationArray = this.executable.getParameterAnnotations();
        int index = this.parameterIndex;
        if (this.executable instanceof Constructor &&
            ClassUtils.isInnerClass(this.executable.getDeclaringClass()) &&
            annotationArray.length == this.executable.getParameterCount() - 1) {
            // Bug in javac in JDK <9: annotation array excludes enclosing instance parameter
            // for inner classes, so access it with the actual parameter index lowered by 1
            index = this.parameterIndex - 1;
        }
        paramAnns = (index >= 0 && index < annotationArray.length ?
                     adaptAnnotationArray(annotationArray[index]) : EMPTY_ANNOTATION_ARRAY);
        this.parameterAnnotations = paramAnns;
    }
    return paramAnns;
}
```





##### 列表 ``org.springframework.validation.DataBinder#validators`` 是如何初始化的？

经过翻看源码，DataBinderFactory和DataBinder 在每次请求都会新创建，每次创建DataBinder 主要通过初始化对象`private final WebBindingInitializer initializer;`装载Validator。经过debug发现这个对象地址是一直不变的，肯定是在应用启动的时候加载的。查找DefaultDataBinderFactory的实例化过程发现他是从处理器适配器中获取的。那么就继续查看适配器的加载过程。

```java
org.springframework.web.bind.support.DefaultDataBinderFactory#createBinder
this.initializer = org.springframework.web.bind.support.WebBindingInitializer
org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#createDataBinderFactory
org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#getWebBindingInitializer
```

```java
public final WebDataBinder createBinder( NativeWebRequest webRequest, @Nullable Object 			target, String objectName) throws Exception {
    WebDataBinder dataBinder = createBinderInstance(target, objectName, webRequest);
    if (this.initializer != null) {
        this.initializer.initBinder(dataBinder, webRequest);
    }
    initBinder(dataBinder, webRequest);
    return dataBinder;
}
```

```java
protected InitBinderDataBinderFactory createDataBinderFactory(List<InvocableHandlerMethod> binderMethods) throws Exception {
	return new ServletRequestDataBinderFactory(binderMethods, getWebBindingInitializer());
	}
```

```java
@Nullable
public WebBindingInitializer getWebBindingInitializer() {
    return this.webBindingInitializer;
}
```

终于在装配对象中发现初始化的地方。那么这个时候就重新回到驱动注解``@EnableWebMvc`` 自动装配上来。

```java
org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#requestMappingHandlerAdapter
```

```java
@Bean
public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
    RequestMappingHandlerAdapter adapter = createRequestMappingHandlerAdapter();
    adapter.setContentNegotiationManager(mvcContentNegotiationManager());
    adapter.setMessageConverters(getMessageConverters());
    adapter.setWebBindingInitializer(getConfigurableWebBindingInitializer());
    adapter.setCustomArgumentResolvers(getArgumentResolvers());
    adapter.setCustomReturnValueHandlers(getReturnValueHandlers());

    if (jackson2Present) {
        adapter.setRequestBodyAdvice(Collections.singletonList(new JsonViewRequestBodyAdvice()));
        adapter.setResponseBodyAdvice(Collections.singletonList(new JsonViewResponseBodyAdvice()));
    }

    AsyncSupportConfigurer configurer = new AsyncSupportConfigurer();
    configureAsyncSupport(configurer);
    if (configurer.getTaskExecutor() != null) {
        adapter.setTaskExecutor(configurer.getTaskExecutor());
    }
    if (configurer.getTimeout() != null) {
        adapter.setAsyncRequestTimeout(configurer.getTimeout());
    }
    adapter.setCallableInterceptors(configurer.getCallableInterceptors());
    adapter.setDeferredResultInterceptors(configurer.getDeferredResultInterceptors());

    return adapter;
}
```



所以可以看出spring-mvc 默认使用的适配器是``RequestMappingHandlerAdapter`` ,实际spring提供了多种适配器，可查看``org.springframework.web.servlet.DispatcherServlet#initHandlerAdapters`` 

```java
private static final Properties defaultStrategies;
private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
static {
    // Load default strategy implementations from properties file.
    // This is currently strictly internal and not meant to be customized
    // by application developers.
    try {
        ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, DispatcherServlet.class);
        defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
    }
    catch (IOException ex) {
        throw new IllegalStateException("Could not load '" + DEFAULT_STRATEGIES_PATH + "': " + ex.getMessage());
    }
}
```

```java
# Default implementation classes for DispatcherServlet's strategy interfaces.
# Used as fallback when no matching beans are found in the DispatcherServlet context.
# Not meant to be customized by application developers.

org.springframework.web.servlet.LocaleResolver=org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver

org.springframework.web.servlet.ThemeResolver=org.springframework.web.servlet.theme.FixedThemeResolver

org.springframework.web.servlet.HandlerMapping=org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping,\
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

org.springframework.web.servlet.HandlerAdapter=org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter,\
	org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter,\
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter

org.springframework.web.servlet.HandlerExceptionResolver=org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver,\
	org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver,\
	org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver

org.springframework.web.servlet.RequestToViewNameTranslator=org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator

org.springframework.web.servlet.ViewResolver=org.springframework.web.servlet.view.InternalResourceViewResolver

org.springframework.web.servlet.FlashMapManager=org.springframework.web.servlet.support.SessionFlashMapManager
```

