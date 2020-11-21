package com.myflx.cache.redis.repository;

import com.myflx.cache.redis.bean.Person;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

/**
 * @see EnableRedisRepositories
 * @see RedisRepositoriesAutoConfiguration
 */
public interface PersonRepository extends CrudRepository<Person, String> {

}
