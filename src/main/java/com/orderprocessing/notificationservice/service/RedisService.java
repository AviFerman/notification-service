package com.orderprocessing.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderprocessing.notificationservice.dto.OrderData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void writeJson(String key, Object value) throws JsonProcessingException {
        String jsonValue = objectMapper.writeValueAsString(value);
        redisTemplate.opsForValue().set(key, jsonValue);
        log.info("writeJson:: Stored JSON in Redis with key: {}", key);

    }

    public OrderData readJson(String key, Class<OrderData> type) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                log.debug("No value found in Redis for key: {}", key);
                return null;
            }
            OrderData orderData = objectMapper.readValue(json, OrderData.class);
            log.debug("Read from Redis - key: {}, value: {}", key, orderData);
            return orderData;
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON from Redis for key: {}", key, e);
            return null;
        }
    }
}
