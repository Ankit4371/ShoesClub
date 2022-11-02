package com.example.OrderService.service;

import com.example.OrderService.dao.OrderDAO;
import com.example.OrderService.dto.InventoryResponse;
import com.example.OrderService.dto.OrderItemDTO;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.event.OrderPlacedEvent;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {

    private final OrderDAO orderDAO;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public OrderService(OrderDAO orderDAO, WebClient.Builder webClientBuilder, Tracer tracer, KafkaTemplate kafkaTemplate) {
        this.orderDAO = orderDAO;
        this.webClientBuilder = webClientBuilder;
        this.tracer = tracer;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderItemList(orderRequest.getOrderItemDTOList()
                .stream().
                map(orderItemDTO -> mapToOrderItem(orderItemDTO))
                .collect(Collectors.toList()));

        List<String> skuCodeList = order.getOrderItemList()
                .stream().
                map(orderItem -> orderItem.getSkuCode()).
                collect(Collectors.toList());

        Span inventoryServiceLookup = tracer.nextSpan().name("inventoryServiceLookup");
        try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){

            InventoryResponse[] inventoryResponseArray = (webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCodeList", skuCodeList).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block()); // sync request

            Boolean allStocksAvailable = false;
            allStocksAvailable = Arrays.asList(inventoryResponseArray).size() > 0 && Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isStockLeft());
            Arrays.asList(inventoryResponseArray).forEach(System.out::println);
            // TODO
            if(allStocksAvailable){
                orderDAO.save(order);
                kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
                // TODO : update stocks in inventoryService
            }else{
                throw new IllegalArgumentException("Product out of Stock");
            }
            log.info("Order Placed with id {}.",order.getId());
            return "Order Places Successfully.";
        }finally {
            inventoryServiceLookup.end();
        }

    }

    private OrderItem mapToOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItem.getQuantity());
        orderItem.setPrice(orderItemDTO.getPrice());
        orderItem.setSkuCode(orderItemDTO.getSkuCode());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItem;

    }
}
