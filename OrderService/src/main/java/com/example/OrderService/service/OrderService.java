package com.example.OrderService.service;

import com.example.OrderService.dao.OrderDAO;
import com.example.OrderService.dto.OrderItemDTO;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }


    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderItemList(orderRequest.getOrderItemDTOList()
                .stream().
                map(orderItemDTO -> mapToOrderItem(orderItemDTO))
                .collect(Collectors.toList()));
        orderDAO.save(order);
        log.info("Order Placed with id {}.",order.getId());
    }

    private OrderItem mapToOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItem.getQuantity());
        orderItem.setPrice(orderItemDTO.getPrice());
        orderItem.setSkuCode(orderItemDTO.getSkuCode());
        orderItem.setQuantity(orderItem.getQuantity());
        return orderItem;

    }
}
