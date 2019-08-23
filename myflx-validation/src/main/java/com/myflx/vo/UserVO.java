package com.myflx.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LuoShangLin
 */
public class UserVO implements Serializable {

    private static final long serialVersionUID = -6084419847851348876L;

    @NotBlank
    private String name;

    @Min(value = 18, message = "对不起，经验不够丰富~")
    @Max(value = 40, message = "sorry~")
    private Integer age;

    private String companyCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
