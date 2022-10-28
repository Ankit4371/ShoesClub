package com.example.OrderService.dto;

import com.example.OrderService.model.OrderItem;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class OrderRequest {

    private List<OrderItemDTO> orderItemDTOList;


}
