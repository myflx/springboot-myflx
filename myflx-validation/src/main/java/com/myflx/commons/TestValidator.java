package com.myflx.commons;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.LongValidator;
import org.apache.commons.validator.routines.PercentValidator;

import java.math.BigDecimal;

/**
 * @author LuoShangLin
 */
public class TestValidator {

    public static void main(String[] args) {
        BigDecimal a = BigDecimalValidator.getInstance().validate("a");
        System.out.println(a);
        a = BigDecimalValidator.getInstance().validate("1.0");
        System.out.println(a);
        BigDecimal validate = PercentValidator.getInstance().validate("10%1");
        System.out.println(validate);
        boolean inRange = LongValidator.getInstance().isInRange(10, 0, 10L);
        System.out.println(inRange);
    }
}
