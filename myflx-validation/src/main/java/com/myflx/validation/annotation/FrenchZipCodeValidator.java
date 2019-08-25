package com.myflx.validation.annotation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FrenchZipCodeValidator implements ConstraintValidator<FrenchZipCode, String> {

    private String countryCode;
    public void initialize(FrenchZipCode constraint) {
        this.countryCode = constraint.countryCode();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.equals(countryCode,"CN") || Objects.equals(countryCode,"us")){
            return true;
        }
        return false;
    }
}
