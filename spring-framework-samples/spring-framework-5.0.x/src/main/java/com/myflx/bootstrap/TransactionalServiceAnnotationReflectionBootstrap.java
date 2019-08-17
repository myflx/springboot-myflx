package com.myflx.bootstrap;


import com.myflx.annotation.TransactionalService;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@TransactionalService(name = "test")
public class TransactionalServiceAnnotationReflectionBootstrap {

    public static void main(String[] args) throws IOException {
        //所有的注解Class都继承了Annotation 所有被注解的Class都继承了AnnotatedElement
        AnnotatedElement annotatedElement = TransactionalServiceAnnotationReflectionBootstrap.class;
        //获取TransactionalService注解
        TransactionalService annotation = annotatedElement.getAnnotation(TransactionalService.class);
        //直接调用 name()
        String name = annotation.name();
        System.out.println("TransactionalService.name() = " + name);

        //反射实现 使用spring 的反射工具处理
        ReflectionUtils.doWithMethods(
                TransactionalService.class,
                method ->
                    System.out.printf(
                            "@TransactionalService.%s()=%s\n",method.getName(),
                            //反射调用方法
                            ReflectionUtils.invokeMethod(method,annotation)
                    ),
                //获取第一个无参方法
                //method -> method.getParameterCount() == 0
                method -> !method.getDeclaringClass().equals(Annotation.class)
        );
    }
}
