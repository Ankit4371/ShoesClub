package com.example.OrderService.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {

    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
