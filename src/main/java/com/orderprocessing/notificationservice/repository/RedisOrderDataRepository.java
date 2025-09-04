package com.orderprocessing.notificationservice.repository;

import com.orderprocessing.notificationservice.dto.OrderData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisOrderDataRepository extends CrudRepository<OrderData, String> {
    Optional<OrderData> findByOrderId(String orderId);
}
