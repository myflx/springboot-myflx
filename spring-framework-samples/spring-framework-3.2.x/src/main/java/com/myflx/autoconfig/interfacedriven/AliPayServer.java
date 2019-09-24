package com.myflx.autoconfig.interfacedriven;

public class AliPayServer implements Pay {
    @Override
    public void beforePay() {
        System.out.println("before AliPay pay");
    }

    @Override
    public void doPay() {
        System.out.println("do AliPay pay");
    }

    @Override
    public void afterPay() {
        System.out.println("after AliPay pay");
    }
}
