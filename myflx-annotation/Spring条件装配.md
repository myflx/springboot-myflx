Spring条件装配

@Profile

自定义Profile条件装配

条件装配原理

- org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#isCandidateComponent(org.springframework.core.type.classreading.MetadataReader)

- org.springframework.context.annotation.ConfigurationClassParser#processConfigurationClass

- org.springframework.context.annotation.AnnotatedBeanDefinitionReader#registerBean(java.lang.Class<?>, java.lang.String, java.lang.Class<? extends java.lang.annotation.Annotation>...)



@Conditional

内建Condition注解归纳

自定义Conditional条件装配

条件装配原理

ProfileCondition

ConditionEvaluator评估两阶段



Profile，Conditional 区别？