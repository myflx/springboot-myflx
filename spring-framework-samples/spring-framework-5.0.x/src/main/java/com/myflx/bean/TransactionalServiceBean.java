package com.myflx.bean;


import com.myflx.annotation.TransactionalService;

@TransactionalService
public class TransactionalServiceBean {

    public void save(){
        System.out.println("保存操作。。。");
    }
}
