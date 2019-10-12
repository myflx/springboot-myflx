package com.myflx.webmvc.initializer;

import com.myflx.webmvc.config.SpringWebMvcConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringWebMvcServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    protected Class<?>[] getServletConfigClasses() {
        return of(SpringWebMvcConfiguration.class);
    }

    protected String[] getServletMappings() {
        return of("/*");
    }

    private static <T> T[] of(T... values) {
        return values;
    }
}
