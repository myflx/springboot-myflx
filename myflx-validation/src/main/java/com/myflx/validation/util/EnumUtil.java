package com.myflx.validation.util;

import com.myflx.validation.IValidateEnum;

public class EnumUtil {
    private EnumUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T extends IValidateEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}