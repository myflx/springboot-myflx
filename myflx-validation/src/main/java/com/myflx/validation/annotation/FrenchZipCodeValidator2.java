package com.myflx.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FrenchZipCodeValidator2 implements ConstraintValidator<FrenchZipCode, String> {
    public void initialize(FrenchZipCode2 constraint) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}
