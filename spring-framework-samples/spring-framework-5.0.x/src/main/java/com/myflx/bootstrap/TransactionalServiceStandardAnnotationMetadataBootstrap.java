package com.myflx.bootstrap;


import com.myflx.annotation.TransactionalService;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.io.IOException;
import java.util.Set;

@TransactionalService
public class TransactionalServiceStandardAnnotationMetadataBootstrap {

    public static void main(String[] args) throws IOException {
        String name = TransactionalServiceStandardAnnotationMetadataBootstrap.class.getName();

        ///构建元数据读取类工厂
        AnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(TransactionalServiceStandardAnnotationMetadataBootstrap.class);
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();

        annotationTypes.forEach(type->{
            Set<String> metaAnnotationTypes = annotationMetadata.getMetaAnnotationTypes(type);
            metaAnnotationTypes.forEach(metaAnnotationType->{
                System.out.printf("注解@%s 元标注%s\n\r",type,metaAnnotationType);
            });
        });
    }
}
