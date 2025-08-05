package com.orderprocessing.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderprocessing.notificationservice.dto.OrderData;
import com.orderprocessing.notificationservice.dto.OrderEvent;
import com.orderprocessing.notificationservice.enums.OrderStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryCheckResultListener {

    private final ObjectMapper objectMapper;
    private final RedisService redisService;

    @KafkaListener(
            topics = "${spring.kafka.topic.inventory-check}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listenToMessage(String message) {
        try {
            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);
            log.info("listenToMessage:: Received Order Event with ID: {}", orderEvent.getOrderId());
            Optional<OrderData> orderDataOptional = Optional.ofNullable(redisService.readJson("order:" + orderEvent.getOrderId(), OrderData.class));
            OrderData orderData = orderDataOptional.get();
            log.info("listenToMessage:: Received OrdedData: {}", orderData);
            log.info("---------------------- N O TI VI C A T I O N ----------------------");
            if (orderData.getOrderStatus().equals(OrderStatusEnum.CANCELLED)) {
                log.info("-- ORDER HAS BEEN CANCELLED --");
            }

            if (orderData.getOrderStatus().equals(OrderStatusEnum.GOOD_TO_GO)) {
                log.info("-- ORDER IS GOOD TO GO  --");
            }

            if (orderData.getOrderStatus().equals(OrderStatusEnum.PENDING)) {
                log.info("-- ORDER IS PENDING --");
            }

            log.info("-- ORDDER DATA --");
            log.info(orderData.toString());
            log.info("---------------------- N O TI VI C A T I O N ----------------------");
        } catch (Exception e) {
            log.error("Error processing inventory check result: {}", e.getMessage(), e);
        }
    }
}
