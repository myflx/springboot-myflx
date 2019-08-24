# Bean-validation

​		JSR-303:为了解决贯穿应用各种层级之间pojo所携带的数据的校验，主要使用注解作为元数据同时类本身也是其元数据，可以通过xml覆盖或者扩展校验，其API可以看做是Java-Beans object model的扩展。该JSR使用友好同时灵活性比较好。



#### 如何定义约束（how constraints are defined）?

除非另有声明Bean Validation api的默认包名 ``javax.validation``

- 约束的构成：一个或者多个约束

- 什么是约束？

  - 由``javax.validation.Constraint``元标注注解
  - 注解保留策略为运行时（``RetentionPolicy.RUNTIME``）

- 注解目标类型

  - ```java
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    ```

    其他目标可以注解但是会被provider 忽略。

  - JSR-303 鼓励开发者使用自定义注解。

- 异常

  - 注解Java doc都需要明确标注了注解的ClassType 否则会抛出异常`UnexpectedTypeException`
  - 一系列的限制器应表意明确，特别注意注解和类型的匹配
  - `ConstraintDefinitionException`一般是由于missing or illegal message or groups elements ，所以定义约束一定要注意属性的完整和合理性。

- 约束属性

  - 不能以valid 打头否则会抛出异常 ``javax.validation.ConstraintDefinitionException: HV000073: Parameters starting with 'valid' are not allowed in a constraint``

  - message  必要属性 一般用于错误信息提示，建议对其进行资源绑定有助于使用国际化，命名建议：约束类全名+.message。
  
  -  groups  必要属性，用于指定约束一起运行的组，通常用于控制约束的顺序和执行校验。默认空数组
  
  -  payload  必要属性，指定约束的负载。默认空数组，每个关联的负载必须继承 ``javax.validation.Payload``，主要用于携带校验客户端消费的元数据。``javax.validation.valueextraction.Unwrapping``用于标记值抽取时是否包装对象。Payload 的使用可移植性不强。
  
    - 负载信息的读取
    
      ``javax.validation.metadata.ConstraintDescriptor``、`javax.validation.ConstraintViolation`、``
    
  - Applying multiple constraints of the same type   
  
    



how a JavaBean class is decorated with annotations to describe constraints?

how to programmatically validate a JavaBean?

how to programmatically validate a JavaBean.