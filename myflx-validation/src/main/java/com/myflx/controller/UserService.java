package com.myflx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import javax.validation.constraints.NotBlank;


/**
 * @author LuoShangLin
 */
@Service
public class UserService implements BaseService {


    @Autowired
    private Validator validator;


    public String getLanguage(@NotBlank(message = "版本不能为空") String version) {
        return "java：" + version;
    }

    @NotBlank(message = "未配置默认版本")
    public String getDefault0() {
        return getDefault();
    }

    public String getDefault() {
        return "";
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
