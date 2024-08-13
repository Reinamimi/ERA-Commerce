package me.emma.orderservice.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer quantity;
}
