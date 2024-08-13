package me.emma.orderservice.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
}
