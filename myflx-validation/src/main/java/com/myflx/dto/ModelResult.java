package com.myflx.dto;


import java.io.Serializable;

/**
 * @author LuoShangLin
 */
public class ModelResult<T> implements Serializable {
    private static final long serialVersionUID = 220664522369710286L;
    private boolean success;
    private String msg;
    private String code;
    private long currentTimeMillis = System.currentTimeMillis();
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
