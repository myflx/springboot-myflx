package com.myflx.advice;

import com.myflx.dto.ModelResult;
import com.myflx.dto.ModelResultClient;
import com.myflx.validation.MyflxParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LuoShangLin
 */
@RestControllerAdvice
public class ErrorHandleAdvice {
    private Logger logger = LoggerFactory.getLogger(ErrorHandleAdvice.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("binder");
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ModelResult errorHandler(Exception e) {
        ModelResult modelResult = new ModelResultClient().failFactory("9001", "系统异常，请重试");
        logger.error("捕获异常,路径：{}", getRequestPath(), e);
        return modelResult;
    }

    private String getRequestPath() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return "";
        }
        HttpServletRequest request = attributes.getRequest();
        if (null == request) {
            return "";
        }
        return request.getRequestURI();
    }


    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ModelResult fieldErrorHandler(MethodArgumentNotValidException e) {
//        logger.debug("参数校验异常! request path:{}, error  message:{}", getRequestPath(), e.getBindingResult().getFieldError().getField() + e.getBindingResult().getFieldError().getDefaultMessage());
        //e.getBindingResult().getFieldError().getField()
        System.out.println("错误字段数量：" + e.getBindingResult().getErrorCount());
//        return new ModelResultClient().failFactory("9002", e.getBindingResult().getFieldError().getField() + e.getBindingResult().getFieldError().getDefaultMessage());
        return new ModelResultClient().failFactory("9002", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ModelResult httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.warn("Request method not supported error! request path:{}, error message:{}", getRequestPath(), e.getMessage());
        return new ModelResultClient().failFactory("9003", "Request method not supported");
    }

    @ResponseBody
    @ExceptionHandler(value = MyflxParamException.class)
    public ModelResult dealMyflxParamException(MyflxParamException e) {
        logger.warn("Request method not supported error! request path:{}, error message:{}", getRequestPath(), e.getMessage());
        return new ModelResultClient().failFactory("9004", "Request method not supported");
    }

}
