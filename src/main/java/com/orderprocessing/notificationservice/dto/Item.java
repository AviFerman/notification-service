package com.orderprocessing.notificationservice.dto;

import com.orderprocessing.notificationservice.enums.CategoryEnum;
import com.orderprocessing.notificationservice.enums.ItemAvailabilityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String productId;
    private Integer quantity;
    private CategoryEnum category;
    private ItemAvailabilityEnum availability;

}
