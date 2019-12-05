package com.myflx.validator;

import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Configuration;

/**
 * @Description TODO
 * @Author LuoShangLin
 * @Date 2019/9/5 18:34
 * @Since V2.13.0
 */
/*@Service*/
public class ConfiguredValidatorFactoryBean extends LocalValidatorFactoryBean {

    /**
     * Post-process the given Bean Validation configuration,
     * adding to or overriding any of its settings.
     * <p>Invoked right before building the {@link ValidatorFactory}.
     *
     * @param configuration the Configuration object, pre-populated with
     *                      settings driven by LocalValidatorFactoryBean's properties
     */
    @Override
    protected void postProcessConfiguration(Configuration<?> configuration) {
        ((ConfigurationImpl) configuration).failFast(true);
        //设置消息截断
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        this.setMessageInterpolator(interpolatorFactory.getObject());
//        ((ConfigurationImpl) configuration).failFast(false);
        super.postProcessConfiguration(configuration);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

    }
}
