package com.myflx.handler;

import com.netflix.discovery.PreRegistrationHandler;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

@Singleton
public class EurekaPreRegistrationHandler implements PreRegistrationHandler {
    @Override
    public void beforeRegistration() {
        System.out.println("com.myflx.discovery.handler.EurekaPreRegistrationHandler.beforeRegistration");
    }
}
