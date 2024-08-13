package me.emma.paymentservice.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Orders {

    private Long orderId;
    private Long userId;
    private Long productId;
    private BigDecimal totalPrice;
    private Integer quantity;
    private Status status;
}
