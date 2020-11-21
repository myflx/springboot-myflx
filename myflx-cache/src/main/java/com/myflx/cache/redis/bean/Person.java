package com.myflx.cache.redis.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("people")
@Data
public class Person {
    @Id
    String id;
    String firstname;
    String lastname;

    public Person(String id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
