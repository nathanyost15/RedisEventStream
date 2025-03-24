package com.example.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EnableAsync
@Component
public class Publisher {
    public static final String KEY = "context-orchestration-preload";
    private static final String GROUP = "aiq-convoy-context-orchestration-service";
    private static final String CONSUMER = "instance-0";
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Async
    @Scheduled(fixedRate = 50)
    public void sendMessage() {
        Map<Object, Object> record = new HashMap<>();
        record.put("accountNumber", "1234123412341234");
        record.put("eventId", UUID.randomUUID().toString());
        redisTemplate.boundStreamOps(KEY).add(record);
        System.out.println("Sent message to queue");
    }
}