package com.myflx.validation.validator;


import com.myflx.validation.IValidateEnum;
import com.myflx.validation.annotation.TypeConstraint;
import com.myflx.validation.util.EnumUtil;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.util.Map;
import java.util.Objects;

/**
 * @author LuoShangLin
 */
public class TypeConstraintValidator implements ConstraintValidator<TypeConstraint, Integer> {

    @Override
    public void initialize(TypeConstraint myFormValidator) {
        System.out.println("初始化：com.myflx.validation.validator.TypeConstraintValidator.initialize");
        final Class<? extends Payload>[] payload = myFormValidator.payload();

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
        } else {
            return true;
        }
    }
}