package com.myflx.validation.annotation;


import com.myflx.validation.IValidateEnum;
import com.myflx.validation.validator.TypeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = TypeConstraintValidator.class)
public @interface TypeConstraint {
    Class<? extends IValidateEnum> type();
    String message() default "传入数据%s不存在";
//    String validA() default "传入数据不存在";
    Class<?>[] groups() default {};
    Class<? extends Payload>[]  payload() default {};
}
