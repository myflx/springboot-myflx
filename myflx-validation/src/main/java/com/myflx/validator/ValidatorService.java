package com.myflx.validator;


import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author LuoShangLin
 */
@Service
public class ValidatorService {
    @Autowired
    private Validator validator;

    public void validBase(Object param) {
        Set<ConstraintViolation<Object>> validate = validator.validate(param);
        AtomicReference<String> err = new AtomicReference<>();
        validate.forEach(addressConstraintViolation -> {
            System.out.println("错误信息"+addressConstraintViolation.getMessage());
        });

    }




    public void valid(Object param) {
        Set<ConstraintViolation<Object>> validate = getValidator().validate(param);
        AtomicReference<String> err = new AtomicReference<>();
        validate.forEach(addressConstraintViolation -> err.set(addressConstraintViolation.getMessage()));
        System.out.println("错误信息"+err.get());

    }

    /**
     * 校验器构造
     * @return o
     */
    public Validator getValidator() {
        return Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast( true )
                .buildValidatorFactory()
                .getValidator();
    }
}
