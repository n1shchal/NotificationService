package com.learn.NotificationService.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {

    private final RedisTemplate redisTemplate;

    public RedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToBlacklist(String number) {

        redisTemplate.opsForHash().put("PhoneNumber", number, "blacklisted");
    }

    public void removeFromBlacklist(String number) {
        redisTemplate.opsForHash().delete("PhoneNumber", number);
    }

    public String check(String number) {

        return (String) redisTemplate.opsForHash().get("PhoneNumber", number);
    }
}
