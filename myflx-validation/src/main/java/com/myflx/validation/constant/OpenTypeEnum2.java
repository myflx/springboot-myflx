package com.myflx.validation.constant;

import com.myflx.validation.IValidateEnum;

/**
 * @author LuoShangLin
 */

public enum OpenTypeEnum2 implements IValidateEnum {

    WX(1,"微信"),ALI(2,"支付宝")
    ;


    OpenTypeEnum2(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    private String message;
    private Integer code;
    /**
     * 枚举值
     *
     * @return Integer
     * @Author LuoShangLin
     * @Date 2019/1/11 15:04
     */
    @Override
    public Integer getCode() {
        return null;
    }

    /**
     * 枚举信息
     *
     * @return String
     * @Author LuoShangLin
     * @Date 2019/1/11 15:04
     */
    @Override
    public String getMessage() {
        return null;
    }
}
