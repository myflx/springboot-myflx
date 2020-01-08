package com.myflx.advice;

import com.google.common.collect.Lists;
import com.myflx.dto.ModelResult;
import com.myflx.dto.ModelResultClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.myflx.advice.ErrorHandlerOrder.FIRST_LEVEL;

/**
 * @author LuoShangLin
 */
@Order(FIRST_LEVEL)
@RestControllerAdvice
public class ErrorHandleAdvice1 {

    private ErrorHandleAdvice1() {
    }

    /*@ExceptionHandler(ConstraintViolationException.class)
    public static ModelResult<Boolean> validHandler(ConstraintViolationException e) {
        final List<ConstraintViolation> violations = Lists.newArrayList(e.getConstraintViolations());
        *//*log.info("校验不通过：{}", violations.get(0).getRootBeanClass().toString() + violations.get(0).getPropertyPath());*//*
        System.out.println("校验不通过：{}" + violations.get(0).getRootBeanClass().toString() +"#"+ violations.get(0).getPropertyPath()+ violations.get(0).getMessage());
        return new ModelResultClient<Boolean>().failFactory("1007", violations.get(0).getMessage());
    }*/

}
