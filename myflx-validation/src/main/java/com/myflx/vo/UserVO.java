package com.myflx.vo;

import com.myflx.validation.IValid;
import com.myflx.validation.MyflxParamException;
import com.myflx.validation.annotation.TypeConstraint;
import com.myflx.validation.annotation.UserConstraint;
import com.myflx.validation.constant.OpenTypeEnum;
import org.springframework.util.StringUtils;

import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LuoShangLin
 */
@UserConstraint(groups = IValid.class)
public class UserVO implements Serializable, Payload ,IValid{

    public interface AddUserGroup {
    }

    private static final long serialVersionUID = -6084419847851348876L;

    @NotBlank
    private String name;

    @Min(value = 18, message = "对不起，经验不够丰富~")
    @Max(value = 40, message = "sorry~")
    private Integer age;

    private String companyCode;

    @TypeConstraint(type = OpenTypeEnum.class, groups = AddUserGroup.class, payload = UserVO.class)
    private Integer openType;

    @TypeConstraint(type = OpenTypeEnum.class, groups = AddUserGroup.class, payload = UserVO.class)
    private Integer openType2;


    @Override
    public void valid() {
        if (StringUtils.isEmpty(name)){
            throw new MyflxParamException(name+":不符合校验规则！");
        }
    }

    public Integer getOpenType2() {
        return openType2;
    }

    public void setOpenType2(Integer openType2) {
        this.openType2 = openType2;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

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
