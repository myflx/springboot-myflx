package com.myflx.validation.annotation;


import com.myflx.validation.IValidateEnum;
import com.myflx.validation.validator.TypeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = TypeConstraintValidator.class)
public @interface TypeConstraint {
    Class<? extends IValidateEnum> type();
    String message() default "传入数据不存在";
    String validA() default "传入数据不存在";
    Class<?>[] groups() default {};
    Class<? extends Payload>[]  payload() default {};
}
