package com.myflx.dto;

/**
 * @description: 封装结果处理类
 * @author: fezs
 * @date: 2018/01/10
 */
public class ModelResultClient<T> {

    public ModelResult<T> successFactory(T t) {
        ModelResult<T> r = new ModelResult<>();
        r.setData(t);
        r.setSuccess(true);
        return r;
    }

    public ModelResult<T> successFactory() {
        return this.successFactory(null);
    }

    public ModelResult<T> failFactory(String code, String msg) {
        ModelResult<T> r = new ModelResult<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public ModelResult<T> failFactory() {
        return this.failFactory("-1", null);
    }

    public ModelResult<T> failFactory(String code) {
        return this.failFactory(null, null);
    }

}
