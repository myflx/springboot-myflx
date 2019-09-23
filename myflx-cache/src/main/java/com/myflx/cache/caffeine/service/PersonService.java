package com.myflx.cache.caffeine.service;


import com.myflx.cache.caffeine.dto.Person;
import com.myflx.cache.caffeine.service.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 服务
 * 
 * @author LuoShangLin
 * @date 2019/9/23 10:16
 * @since V1.0
 **/
@Slf4j
@Service
public class PersonService {

    @Autowired
    private PersonMapper personMapper;


    @Cacheable(value = "person",key = "#id")
    public Person getOne(int id) {
        log.info("load one person");
        return personMapper.selectOne(id);
    }

    @CacheEvict(value = "person", key = "#person.id")
    public void update(Person person) {
        personMapper.updateById(person);
    }
}
