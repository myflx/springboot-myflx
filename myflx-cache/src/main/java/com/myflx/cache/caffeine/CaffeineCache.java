package com.myflx.cache.caffeine;

import com.myflx.cache.caffeine.dto.Person;
import com.myflx.cache.caffeine.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoShangLin
 */
@RestController
public class CaffeineCache {
    private final PersonService personService;

    @Autowired
    public CaffeineCache(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/getPerson")
    public Person getPerson() {
        return personService.getOne(1);
    }
}
