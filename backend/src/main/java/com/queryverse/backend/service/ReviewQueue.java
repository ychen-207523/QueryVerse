package com.queryverse.backend.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class ReviewQueue {
    private final RedisTemplate<String, String> redis;

    public ReviewQueue(RedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    /** ZSET key for a user's due queue */
    private String key(long userId) {
        return "qv:v1:user:%d:due:z".formatted(userId);
    }

    public void schedule(long userId, long questionId, Instant dueAt) {
        redis.opsForZSet().add(key(userId), String.valueOf(questionId), dueAt.toEpochMilli());
    }


    public List<Long> fetchDue(long userId, int limit, Instant now) {
        Set<String> ids = redis.opsForZSet().rangeByScore(key(userId), 0, now.toEpochMilli(), 0, limit);
        if (ids == null || ids.isEmpty()) return List.of();
        return ids.stream().map(Long::valueOf).toList();
    }

    public void remove(long userId, long questionId) {
        redis.opsForZSet().remove(key(userId), String.valueOf(questionId));
    }
}
