package com.example.InventoryService.controller;

import com.example.InventoryService.dto.InventoryResponse;
import com.example.InventoryService.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory/skuCodeList=abc1&skuCodeList=abc2
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> haveStocks(@RequestParam List<String> skuCodeList) throws InterruptedException {

        return inventoryService.hasStocks(skuCodeList);
    }
}
