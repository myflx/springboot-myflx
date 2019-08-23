package com.myflx.validation.validator;


import com.myflx.validation.IValidateEnum;
import com.myflx.validation.annotation.TypeConstraint;
import com.myflx.validation.util.EnumUtil;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author LuoShangLin
 */
public class TypeConstraintValidator implements ConstraintValidator<TypeConstraint, Integer>, Annotation {


    private String message;

    private Class<? extends IValidateEnum> typeEnumClass;

    @Override
    public void initialize(TypeConstraint myFormValidator) {
        typeEnumClass = myFormValidator.type();
        message = myFormValidator.message();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        String format = String.format(message, value);
        context.buildConstraintViolationWithTemplate(format).addConstraintViolation();
        if (Objects.nonNull(value)) {
            IValidateEnum typeEnum = EnumUtil.getByCode(value, typeEnumClass);
            return Objects.nonNull(typeEnum);
        }
        return true;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return TypeConstraint.class;
    }
}