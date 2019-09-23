package com.myflx.cache.caffeine.dto;


import lombok.Data;

/**
 * @author LuoShangLin
 */
@Data
public class Person {
    private Integer id;

    private String name;

    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person() {
    }
}
