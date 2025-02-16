package com.myflx.vo;

import com.myflx.validation.annotation.TypeConstraint;
import com.myflx.validation.constant.OpenTypeEnum;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LuoShangLin
 */
public class UserVO2 implements Serializable {

    public interface AddUserGroup {
    }

    public interface DigitGroup {
    }

    public interface I18nGroup {
    }
    private static final long serialVersionUID = 594502982799815861L;

    @TypeConstraint(type = OpenTypeEnum.class, groups = AddUserGroup.class)
    private Integer openType;

    @TypeConstraint(type = OpenTypeEnum.class, groups = AddUserGroup.class)
    private Integer openType2;

    @Digits(integer = 20,fraction = 2,message = "RMB格式错误",groups = DigitGroup.class)
    private Double money;

    @Digits(integer = 32,fraction = 0,message = "必须为不超过{integer}位的数字",groups = DigitGroup.class)
    private String uuid;

    @NotBlank(message = "国际化信息（{javax.validation.constraints.NotBlank.message}）",groups = I18nGroup.class)
    private String i18n;

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public Integer getOpenType2() {
        return openType2;
    }

    public void setOpenType2(Integer openType2) {
        this.openType2 = openType2;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getI18n() {
        return i18n;
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }
}
