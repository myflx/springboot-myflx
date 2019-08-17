package com.myflx.bootstrap;


import com.myflx.annotation.TransactionalService;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@TransactionalService
public class TransactionalServiceStandardAnnotationMetadataSuperBootstrap {

    public static void main(String[] args) throws IOException {

        ///构建元数据读取类工厂
        AnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(TransactionalServiceStandardAnnotationMetadataSuperBootstrap.class);
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes().stream()
                //循环获取所有元注解类型
                .map(annotationMetadata::getMetaAnnotationTypes)
                .collect(LinkedHashSet::new,Set::addAll,Set::addAll);
        System.out.println("类型列表："+annotationTypes);
        annotationTypes.forEach(type->{
            //获取所有元注解的类型和属性信息 过滤掉有属性的注解
            Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(type);
            if (!CollectionUtils.isEmpty(annotationAttributes)){
                annotationAttributes.forEach((name,value)->{
                    System.out.printf("注解@%s 属性 %s = %s \n",
                            ClassUtils.getShortName(type),name,value
                    );
                });
            }
        });
    }
}
