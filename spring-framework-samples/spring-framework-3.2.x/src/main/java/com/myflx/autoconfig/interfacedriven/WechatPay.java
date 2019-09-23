package com.myflx.autoconfig.interfacedriven;

public class WechatPay implements Pay {

    @Override
    public void beforePay() {
        System.out.println("before wechat pay");
    }

    @Override
    public void doPay() {
        System.out.println("do wechat pay");
    }

    @Override
    public void afterPay() {
        System.out.println("after wechat pay");
    }
}
