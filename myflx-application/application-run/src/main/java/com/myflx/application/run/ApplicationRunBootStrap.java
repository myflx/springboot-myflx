package com.myflx.application.run;

import com.myflx.application.run.args.MyBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author LuoShangLin
 */
@SpringBootApplication
public class ApplicationRunBootStrap {
    public static void main(String[] args) {
        args = new String[]{"--info","logfile.txt","--info=123"};
        System.setProperty("spring.beaninfo.ignore","false");
        final ConfigurableApplicationContext run = SpringApplication.run(ApplicationRunBootStrap.class, args);
        final MyBean myBean = run.getBean(MyBean.class);
        System.out.println(myBean);
        System.out.println(myBean.getDebug());
    }
}
