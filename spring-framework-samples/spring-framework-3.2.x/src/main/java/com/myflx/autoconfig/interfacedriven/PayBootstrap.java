package com.myflx.autoconfig.interfacedriven;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//@EnablePay(type = Pay.Type.ALI)
@EnablePay(type = Pay.Type.WECHAT)
public class PayBootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext();
        configApplicationContext.register(PayBootstrap.class);
        configApplicationContext.refresh();
        Pay bean = configApplicationContext.getBean(Pay.class);
        bean.beforePay();
        bean.doPay();
        bean.afterPay();
    }
}
