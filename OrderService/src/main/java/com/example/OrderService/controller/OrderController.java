package com.example.OrderService.controller;

import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventoryX",fallbackMethod = "fallBack")
    @TimeLimiter(name="inventoryX")
    @Retry(name="inventoryX")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
    }
    public CompletableFuture<String> fallBack(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Can't connect with Inventory right now.");
    }
}
