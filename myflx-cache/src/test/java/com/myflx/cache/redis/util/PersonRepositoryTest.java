package com.myflx.cache.redis.util;

import com.myflx.BaseTest;
import com.myflx.cache.redis.bean.Person;
import com.myflx.cache.redis.repository.PersonRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PersonRepositoryTest extends BaseTest {
    @Autowired
    private PersonRepository repo;

    @Test
    public void basicCrudOperations() {
        Person rand = new Person("1", "rand", "al'thor");
        repo.save(rand);
        Optional<Person> byId = repo.findById(rand.getId());
        if (byId.isPresent()) {
            Person person = byId.get();
            System.out.println(person);
        }
        long count = repo.count();
        System.out.println(count);
        repo.delete(rand);
        Optional<Person> byId1 = repo.findById(rand.getId());
        System.out.println(byId1.isPresent());
    }
}
