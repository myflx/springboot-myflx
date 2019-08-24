package com.myflx.validation.annotation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FrenchZipCodeValidator implements ConstraintValidator<FrenchZipCode, String> {
    public void initialize(FrenchZipCode constraint) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}
