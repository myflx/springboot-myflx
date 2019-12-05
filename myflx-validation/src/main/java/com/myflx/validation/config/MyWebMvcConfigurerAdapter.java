package com.myflx.validation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WebMvcConfigurerAdapter 被废弃
 *
 * @author LuoShangLin
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Autowired
    private LocalValidatorFactoryBean defaultValidator;

    public ResourceBundleMessageSource getMessageSource() throws Exception {
        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
        rbms.setDefaultEncoding("UTF-8");
        // 此为文件目录 ValidationMessages是文件名
        rbms.setBasenames("i18n/error/ValidationMessages");
        rbms.setCacheSeconds(10);
        return rbms;
    }

    @Override
    public Validator getValidator() {
        try {
            defaultValidator.setValidationMessageSource(getMessageSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValidator;
    }
}
