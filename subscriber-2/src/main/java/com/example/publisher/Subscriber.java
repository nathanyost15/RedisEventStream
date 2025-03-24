package com.example.publisher;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class Subscriber {

    public static final String KEY = "context-orchestration-preload";
    private static final String GROUP = "aiq-convoy-context-orchestration-service";
    private static final String CONSUMER = "instance-1";
    private static final int BATCH_SIZE = 20;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void consumeMessages() {
        try {
            redisTemplate.opsForStream().createGroup(KEY, ReadOffset.from("0"), GROUP);
        } catch (Exception e) {
            // Group already exists, ignore
        }

        new Thread(() -> {
            while (true) {
                StreamReadOptions streamReadOptions = StreamReadOptions.empty();
                streamReadOptions = streamReadOptions.autoAcknowledge();
                streamReadOptions = streamReadOptions.count(BATCH_SIZE);
                streamReadOptions = streamReadOptions.block(Duration.ZERO);
                List<MapRecord<String, Object, Object>> response = redisTemplate.opsForStream().read(
                        Consumer.from(GROUP, CONSUMER),
                        streamReadOptions,
                        StreamOffset.create(KEY, ReadOffset.lastConsumed())
                );

                if (response != null && !response.isEmpty()) {
                    for (MapRecord<String, Object, Object> record : response) {
                        System.out.println("Received message: " + record.getValue());
//                        redisTemplate.opsForStream().acknowledge(KEY, GROUP, record.getId());
                    }
                }
            }
        }).start();
    }
}