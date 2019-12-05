package com.myflx.controller;


import org.springframework.validation.annotation.Validated;

import javax.validation.Validator;

/**
 * @author LuoShangLin
 */
@Validated
public interface BaseService {

    Validator getValidator();


}
