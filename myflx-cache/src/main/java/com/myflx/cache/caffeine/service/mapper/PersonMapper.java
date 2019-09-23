package com.myflx.cache.caffeine.service.mapper;


import com.myflx.cache.caffeine.dto.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author LuoShangLin
 */
@Repository
@Slf4j
public class PersonMapper {
    public Person selectOne(int id) {
        log.info("com.myflx.cache.caffeine.service.mapper.PersonMapper.selectOne");
        return new Person(id, "name");
    }

    public void updateById(Person person) {
        log.info("更新：person：" + person);
        log.info("com.myflx.cache.caffeine.service.mapper.PersonMapper.updateById");
    }
}
