package com.myflx.validation.annotation;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@NotNull
@Size(min=1)
@ReportAsSingleViolation
@Constraint(validatedBy = NotEmpty.NotEmptyValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface NotEmpty {
    String message() default "{com.myflx.validation.annotation.NotEmpty.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotEmpty[] value();
    }
    class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {
        public void initialize(NotEmpty constraintAnnotation) {}
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return true;
        }
    }
}