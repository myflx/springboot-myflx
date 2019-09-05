package com.myflx.vo;

import com.myflx.validation.annotation.TypeConstraint;
import com.myflx.validation.constant.OpenTypeEnum;

import java.io.Serializable;

/**
 * @author LuoShangLin
 */
public class UserVO2 implements Serializable {

    public interface AddUserGroup {
    }
    private static final long serialVersionUID = 594502982799815861L;

    @TypeConstraint(type = OpenTypeEnum.class, groups = AddUserGroup.class)
    private Integer openType;

    @TypeConstraint(type = OpenTypeEnum.class, groups = AddUserGroup.class)
    private Integer openType2;

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
}
