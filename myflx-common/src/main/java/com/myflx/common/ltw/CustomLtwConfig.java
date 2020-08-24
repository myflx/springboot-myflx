package com.myflx.common.ltw;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.AUTODETECT;
import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.ENABLED;

@Configuration
@ComponentScan("com.myflx.common.ltw")
@EnableLoadTimeWeaving(aspectjWeaving = ENABLED)
//@EnableAspectJAutoProxy
public class CustomLtwConfig {
}
