package com.orderprocessing.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderprocessing.notificationservice.dto.OrderData;
import com.orderprocessing.notificationservice.dto.OrderEvent;
import com.orderprocessing.notificationservice.repository.RedisOrderDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryCheckResultListener {

    private final ObjectMapper objectMapper;
    private final RedisOrderDataRepository redisOrderDataRepository;

    @KafkaListener(
            topics = "${spring.kafka.topic.inventory-check}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listenToMessage(String message) {
        try {
            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);
            log.info("listenToMessage:: Received Order Event with ID: {}", orderEvent.getOrderId());
            OrderData orderData = redisOrderDataRepository.findById(orderEvent.getOrderId())
                    .orElseThrow(() -> new IllegalStateException("Order data not found for ID: " + orderEvent.getOrderId()));
            log.info("listenToMessage:: Received OrdedData: {}", orderData);
            log.info("---------------------- N O T I F I C A T I O N ----------------------");
            log.info("ORDDER DATA:");
            log.info("ORDER ID: {}", orderData.getOrderId());
            log.info("ORDER STATUS: {}", orderData.getOrderStatus());
            orderData.getItems().forEach(item -> log.info("ORDER ITEM: {}", item));
            log.info("");
            log.info(orderData.toString());
            log.info("---------------------- N O TI VI C A T I O N ----------------------");
        } catch (Exception e) {
            log.error("Error processing inventory check result: {}", e.getMessage(), e);
        }
    }
}
