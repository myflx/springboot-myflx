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
  
- 双重约束（Applying multiple constraints of the same type   ）

  ​	同一约束的多重限制，可移植性不强，针对不同场景做不同的校验分支。不过官方不推荐使用。
  
  ```java
  @Documented
  @Constraint(validatedBy = ZipCodeValidator.class)
  @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
  @Retention(RUNTIME)
  public @interface ZipCode {
      String countryCode();
      String message() default "{com.acme.constraint.ZipCode.message}";
      Class<?>[] groups() default {};
      Class<? extends Payload>[] payload() default {};
      @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
      @Retention(RUNTIME)
      @Documented
      @interface List {
          ZipCode[] value();
      }
  }
  ```
  
  ```java
  public class Address {
   @ZipCode.List( {
     @ZipCode(countryCode="fr", groups=Default.class,
              message = "zip code is not valid"),
          @ZipCode(countryCode="fr", groups=SuperUser.class,
          message = "zip code invalid. Requires overriding before saving.")
      } )
   private String zipcode;
  }
  ```
  
  
  
- 组合约束

  - 使用

  	>运行使用多个约束合成更高级别的约束，有以下优点：
  	>
  	>1，避免自定义中的多余复制操作，促进基础约束的复用。
  	>
  	>2，将基础注解作为合成元注解的一部分，提高tool awareness.

  ​	组合约束注解的所有元约束注解都会递归校验，生成校验报告。主注解的组会被继承，组合注解的约束被忽略。 payload 同理。所有约束的约束目标类型要匹配。

  - 单报告
   
   > 使用元注解@ReportAsSingleViolation`` 的标注只生成单个报告。特性是只要注解校验失败存一，其他注解的错误报告都会被忽略，直接生成主注解错误报告。不加该注解会校验会生成所有约束的报告，并且报告顺序是随机的（why? 需要看Validator的实现）。
   
  - 属性覆盖
  
  >• OverridesAttribute.constraint
  >• OverridesAttribute.name
  >• OverridesAttribute.constraintIndex
  
- 校验器``javax.validation.ConstraintValidator``  

  ​        校验器和约束直接可通过注解`@Constraint(validatedBy = FrenchZipCodeValidator.class)`显示声明，也可`@Constraint(validatedBy = {}) ` api会自动发现，只要继承ConstraintValidator ，一种策略模式。

  ​		校验器的生命周期该标准是未定义的但允许通过工厂实现类对其生命周期进行维护入spring对jsr303中的工厂实现 ：

  `org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory`

  - 线程安全

    `javax.validation.ConstraintValidator#isValid` 使用时候是要注意线程安全的，

    尽管在spring中的ConstraintValidator的工厂实现

    `org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory`使用的是原型模式，但是，spring 整合的hibernate-validator又会将校验器缓存起来，所以观察每次请求打印出来的地址可知是同一个对象，且initialize() 只会被调用一次。

    缓存位置：

    > ``org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManagerImpl#constraintValidatorCache``

    ```java
    public class SpringConstraintValidatorFactory implements ConstraintValidatorFactory {
        ...
        private final AutowireCapableBeanFactory beanFactory;
        ...
        @Override
        public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            return this.beanFactory.createBean(key);
        }
        ...
    }
    	@Override
    	@SuppressWarnings("unchecked")
    public <T> T createBean(Class<T> beanClass) throws BeansException {
        // Use prototype bean definition, to avoid registering bean as dependent bean.
        RootBeanDefinition bd = new RootBeanDefinition(beanClass);
        bd.setScope(SCOPE_PROTOTYPE);
        bd.allowCaching = ClassUtils.isCacheSafe(beanClass, getBeanClassLoader());
        // For the nullability warning, see the elaboration in AbstractBeanFactory.doGetBean;
        // in short: This is never going to be null unless user-declared code enforces null.
        return (T) createBean(beanClass.getName(), bd, null);
    }
    ```

    

- 约束语义设计

  ​		标准建议单一的语义设计，例如非空校验和长度的校验，语义分开。否则就造成约束的多重含义和导致无法满足实际场景。标准无法强制限制实际设计中的语义。

- 校验上下文 `javax.validation.ConstraintValidatorContext`

  可以重新构建在原有约束上解析出来的约束违背信息。然后在所有的约束违背信息中随机取一个展示，所以重新定义约束违背信息的时候要禁用掉默认的信息。

  ```java
  public class TypeConstraintValidator implements ConstraintValidator<TypeConstraint, Integer>, Annotation {
      @Override
      public void initialize(TypeConstraint myFormValidator) {
          System.out.println("初始化：com.myflx.validation.validator.TypeConstraintValidator.initialize");
      }
      @Override
      public boolean isValid(Integer value, ConstraintValidatorContext context) {
          System.out.println(this);
          String message = ((ConstraintValidatorContextImpl) context).getConstraintDescriptor().getMessageTemplate();
          Map<String, Object> attributes = ((ConstraintValidatorContextImpl) context).getConstraintDescriptor().getAttributes();
          Class<? extends IValidateEnum> typeEnumClass = (Class<? extends IValidateEnum>) attributes.get("type");
          IValidateEnum typeEnum = EnumUtil.getByCode(value, typeEnumClass);
          if (Objects.nonNull(value) && Objects.isNull(typeEnum)) {
            String format = String.format(message, value);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(format).addConstraintViolation();
            return false;
          }else {
              return true;
          }
      }
      @Override
      public Class<? extends Annotation> annotationType() {
          return TypeConstraint.class;
      }
  }
  ```

  

- 

how a JavaBean class is decorated with annotations to describe constraints?

how to programmatically validate a JavaBean?

how to programmatically validate a JavaBean.