package me.emma.orderservice.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
