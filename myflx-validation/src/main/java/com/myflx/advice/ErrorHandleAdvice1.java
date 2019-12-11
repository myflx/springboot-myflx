package com.myflx.advice;

import com.google.common.collect.Lists;
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

    @ExceptionHandler(ConstraintViolationException.class)
    public static String validHandler(ConstraintViolationException e) {
        final List<ConstraintViolation> objects = Lists.newArrayList();
        objects.addAll(e.getConstraintViolations());
        /*System.out.println(1/0);*/
        return objects.get(0).getMessage();
    }

}
