package com.myflx.validation.annotation;

import com.myflx.validation.validator.UserConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({PARAMETER, ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = {UserConstraintValidator.class})
public @interface UserConstraint {
    String message() default "数据有误！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
