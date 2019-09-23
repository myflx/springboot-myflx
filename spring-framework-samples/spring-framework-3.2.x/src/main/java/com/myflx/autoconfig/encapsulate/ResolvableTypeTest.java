package com.myflx.autoconfig.encapsulate;

import org.springframework.core.ResolvableType;

public class ResolvableTypeTest {
    public static void main(String[] args) throws NoSuchFieldException {
        ResolvableType t = ResolvableType.forField(UserDTO.class.getDeclaredField("myMap"));
        t.getSuperType(); // AbstractMap&lt;Integer, List&lt;String&gt;&gt;
        t.asMap(); // Map&lt;Integer, List&lt;String&gt;&gt;
        t.getGeneric(0).resolve(); // Integer
        t.getGeneric(1).resolve(); // List
        t.getGeneric(1); // List&lt;String&gt;
        t.resolveGeneric(1, 0); // String
        System.out.println(t.getRawClass());
    }
}
