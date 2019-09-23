package com.myflx.autoconfig.interfacedriven;

public interface Pay {
    void beforePay();
    void doPay();
    void afterPay();
    enum Type{
        ALI, WECHAT
    }
}
