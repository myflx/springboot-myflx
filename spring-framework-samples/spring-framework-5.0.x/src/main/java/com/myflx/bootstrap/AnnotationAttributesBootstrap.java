package com.myflx.bootstrap;

import com.myflx.annotation.TransactionalService;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.AnnotatedElement;

public class AnnotationAttributesBootstrap {
    public static void main(String[] args) {
        AnnotatedElement annotatedElement = TransactionalService.class;
        AnnotationAttributes serviceAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(annotatedElement, Service.class);
        AnnotationAttributes transactionalAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(annotatedElement, Transactional.class);
        // 输出
        print(serviceAttributes);

        print(transactionalAttributes);
    }

    private static void print(AnnotationAttributes annotationAttributes) {

        System.out.printf("注解 @%s 属性集合 : \n", annotationAttributes.annotationType().getName());

        annotationAttributes.forEach((name, value) ->
                System.out.printf("\t属性 %s : %s \n", name, value)
        );
    }
}
