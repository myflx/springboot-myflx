package com.myflx.bootstrap;


import com.myfllx.annotation.TransactionalService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TransactionalService(name = "test")
public class TransactionalServiceSuperAnnotationReflectBootstrap {

    public static void main(String[] args) throws IOException {
        //所有的注解Class都继承了Annotation 所有被注解的Class都继承了AnnotatedElement
        AnnotatedElement annotatedElement = TransactionalServiceSuperAnnotationReflectBootstrap.class;
        //获取TransactionalService注解
        TransactionalService annotation = annotatedElement.getAnnotation(TransactionalService.class);
        Set<Annotation> metaAnnotations = getAllAnnotations(annotation);
        metaAnnotations.forEach(TransactionalServiceSuperAnnotationReflectBootstrap::printAttribute);
    }

    private static Set<Annotation> getAllAnnotations(Annotation annotation) {
        //返回注解对象的Class类型
        Class<? extends Annotation> annotationType = annotation.annotationType();
        //获取注解的所有注解
        Annotation[] annotations = annotationType.getAnnotations();
        if (ObjectUtils.isEmpty(annotations)){
            return Collections.EMPTY_SET;
        }
        Set<Annotation> metaAnnotationSet = Stream.of(annotations).filter(
                //排除根注解
                metaDataAnnotation -> !Target.class.getPackage().equals(metaDataAnnotation.annotationType().getPackage())
        ).collect(Collectors.toSet());
        //将获取到的注解的元注解 递归查询注解
        Set<Annotation> metaAnnotationAnnotationSet = metaAnnotationSet.stream().map(TransactionalServiceSuperAnnotationReflectBootstrap::getAllAnnotations).collect(
                HashSet::new, Set::addAll, Set::addAll
        );
        metaAnnotationSet.addAll(metaAnnotationAnnotationSet);
        return metaAnnotationSet;
    }

    private static void printAttribute(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        ReflectionUtils.doWithMethods(
                annotationType,
                method ->
                        System.out.printf(
                                "@%s.%s()=%s\n",
                                annotationType.getSimpleName(),method.getName(),
                                //反射调用方法
                                ReflectionUtils.invokeMethod(method,annotation)
                        ),
                //获取第一个无参方法
                //method -> method.getParameterCount() == 0
                //选择非注解方法
                method -> !method.getDeclaringClass().equals(Annotation.class)
        );
    }
}
