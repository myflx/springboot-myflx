package com.myflx.autoconfig.interfacedriven;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class ImportServerSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = getAttributes(importingClassMetadata);
        Pay.Type type = (Pay.Type)attributes.get("type");
        switch (type){
            case WECHAT:
                return new String[]{WechatPay.class.getName()};
            case ALI:
                return new String[]{AliPayServer.class.getName()};
        }
        return new String[0];
    }

    protected AnnotationAttributes getAttributes(AnnotationMetadata metadata) {
        String name = EnablePay.class.getName();
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(name, true));
        Assert.notNull(attributes,
                () -> "No auto-configuration attributes found. Is "
                        + metadata.getClassName() + " annotated with "
                        + ClassUtils.getShortName(name) + "?");
        return attributes;
    }
}
