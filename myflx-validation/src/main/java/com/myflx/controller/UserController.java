package com.myflx.controller;

import com.myflx.vo.UserVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 控制器
 *
 * @author LuoShangLin
 * @date 2019/8/23 21:27
 * @since 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/create")
    public UserVO hello(@Valid @RequestBody UserVO user) {
        return user;
    }
}
