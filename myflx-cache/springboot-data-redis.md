### spring-data-redis

#### redis是如何自动配置的？

spring-autoconfiure-starter 根据依赖





#### lettuce 和 redis 的对比？





#### ``@EnableRedisRepositories`` 是如何工作的？

``org.springframework.data.redis.repository.configuration.RedisRepositoriesRegistrar``

``org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport#registerBeanDefinitions``

``org.springframework.data.repository.config.RepositoryConfigurationDelegate#registerRepositoriesIn``

注册仓库：

```java
public List<BeanComponentDefinition> registerRepositoriesIn(BeanDefinitionRegistry registry,
                                                            RepositoryConfigurationExtension extension) {

    extension.registerBeansForRoot(registry, configurationSource);

    RepositoryBeanDefinitionBuilder builder = new RepositoryBeanDefinitionBuilder(registry, extension, resourceLoader,
                                                                                  environment);
    List<BeanComponentDefinition> definitions = new ArrayList<>();

    for (RepositoryConfiguration<? extends RepositoryConfigurationSource> configuration : extension
         .getRepositoryConfigurations(configurationSource, resourceLoader, inMultiStoreMode)) {

        BeanDefinitionBuilder definitionBuilder = builder.build(configuration);

        extension.postProcess(definitionBuilder, configurationSource);

        if (isXml) {
            extension.postProcess(definitionBuilder, (XmlRepositoryConfigurationSource) configurationSource);
        } else {
            extension.postProcess(definitionBuilder, (AnnotationRepositoryConfigurationSource) configurationSource);
        }

        AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
        String beanName = configurationSource.generateBeanName(beanDefinition);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(REPOSITORY_REGISTRATION, extension.getModuleName(), beanName,
                         configuration.getRepositoryInterface(), configuration.getRepositoryFactoryBeanClassName());
        }

        beanDefinition.setAttribute(FACTORY_BEAN_OBJECT_TYPE, configuration.getRepositoryInterface());

        registry.registerBeanDefinition(beanName, beanDefinition);
        definitions.add(new BeanComponentDefinition(beanDefinition, beanName));
    }

    return definitions;
}
```



获取所有实现类

```java
public Streamable<BeanDefinition> getCandidates(ResourceLoader loader) {
    RepositoryComponentProvider scanner = new RepositoryComponentProvider(getIncludeFilters(), registry);
    scanner.setConsiderNestedRepositoryInterfaces(shouldConsiderNestedRepositories());
    scanner.setEnvironment(environment);
    scanner.setResourceLoader(loader);

    getExcludeFilters().forEach(it -> scanner.addExcludeFilter(it));

    return Streamable.of(() -> getBasePackages().stream()//
                         .flatMap(it -> scanner.findCandidateComponents(it).stream()));
}
```

构建代理类的bean 定义

```java
//BeanDefinitionBuilder definitionBuilder = builder.build(configuration);
public BeanDefinitionBuilder build(RepositoryConfiguration<?> configuration) {

    Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
    Assert.notNull(resourceLoader, "ResourceLoader must not be null!");

    BeanDefinitionBuilder builder = BeanDefinitionBuilder
        .rootBeanDefinition(configuration.getRepositoryFactoryBeanClassName());

    builder.getRawBeanDefinition().setSource(configuration.getSource());
    builder.addConstructorArgValue(configuration.getRepositoryInterface());
    builder.addPropertyValue("queryLookupStrategyKey", configuration.getQueryLookupStrategyKey());
    builder.addPropertyValue("lazyInit", configuration.isLazyInit());

    configuration.getRepositoryBaseClassName()//
        .ifPresent(it -> builder.addPropertyValue("repositoryBaseClass", it));

    NamedQueriesBeanDefinitionBuilder definitionBuilder = new NamedQueriesBeanDefinitionBuilder(
        extension.getDefaultNamedQueryLocation());
    configuration.getNamedQueriesLocation().ifPresent(definitionBuilder::setLocations);

    builder.addPropertyValue("namedQueries", definitionBuilder.build(configuration.getSource()));

    registerCustomImplementation(configuration).ifPresent(it -> {
        builder.addPropertyReference("customImplementation", it);
        builder.addDependsOn(it);
    });

    BeanDefinitionBuilder fragmentsBuilder = BeanDefinitionBuilder
        .rootBeanDefinition(RepositoryFragmentsFactoryBean.class);

    List<String> fragmentBeanNames = registerRepositoryFragmentsImplementation(configuration) //
        .map(RepositoryFragmentConfiguration::getFragmentBeanName) //
        .collect(Collectors.toList());

    fragmentsBuilder.addConstructorArgValue(fragmentBeanNames);

    builder.addPropertyValue("repositoryFragments",
                             ParsingUtils.getSourceBeanDefinition(fragmentsBuilder, configuration.getSource()));

    RootBeanDefinition evaluationContextProviderDefinition = new RootBeanDefinition(
        ExtensionAwareEvaluationContextProvider.class);
    evaluationContextProviderDefinition.setSource(configuration.getSource());

    builder.addPropertyValue("evaluationContextProvider", evaluationContextProviderDefinition);

    return builder;
}
```

