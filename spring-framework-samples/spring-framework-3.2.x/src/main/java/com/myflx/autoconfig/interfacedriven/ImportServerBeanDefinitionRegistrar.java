package com.myflx.autoconfig.interfacedriven;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class ImportServerBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ImportServerSelector importServerSelector = new ImportServerSelector();
        String[] strings = importServerSelector.selectImports(importingClassMetadata);
        for (String className : strings) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(className);
            AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
            BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition,registry);
        }
    }
}
