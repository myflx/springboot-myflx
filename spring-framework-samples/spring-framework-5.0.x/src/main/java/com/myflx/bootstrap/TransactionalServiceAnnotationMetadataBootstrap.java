package com.myflx.bootstrap;


import com.myfllx.annotation.TransactionalService;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.Set;

@TransactionalService
public class TransactionalServiceAnnotationMetadataBootstrap {

    public static void main(String[] args) throws IOException {
        String name = TransactionalServiceAnnotationMetadataBootstrap.class.getName();

        ///构建元数据读取类工厂
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader = readerFactory.getMetadataReader(name);

        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();

        annotationTypes.forEach(type->{
            Set<String> metaAnnotationTypes = annotationMetadata.getMetaAnnotationTypes(type);
            metaAnnotationTypes.forEach(metaAnnotationType->{
                System.out.printf("注解@%s 元标注%s\n\r",type,metaAnnotationType);
            });
        });
    }
}
