package com.myflx.validation.processor;

import com.myflx.validator.ConfiguredValidatorFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ValidationFailFastPostProcessor
 * 后置处理器
 *
 * @author LuoShangLin
 * @date 2019/12/5 15:21
 * @since V2.3.0
 **/
public class ValidationFailFastPostProcessor implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(ConfiguredValidatorFactoryBean.class);
        definition.setPrimary(true);
        registry.registerBeanDefinition("customerValidator", definition);
    }
}
