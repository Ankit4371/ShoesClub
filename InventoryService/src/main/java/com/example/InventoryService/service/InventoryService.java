package com.example.InventoryService.service;

import com.example.InventoryService.dao.InventoryDAO;
import com.example.InventoryService.dto.InventoryResponse;
import com.example.InventoryService.model.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryDAO inventoryDAO;

    @Transactional(readOnly = true)
    public List<InventoryResponse> hasStocks(List<String> skuCodeList) throws InterruptedException {
        log.info("Stocks Checking ");
//        Thread.sleep(10000);
        return inventoryDAO.findBySkuCodeIn(skuCodeList).stream()
                .map(inventory -> mapToInventoryResponse(inventory))
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        return InventoryResponse
                .builder()
                .skuCode(inventory.getSkuCode())
                .stockLeft(inventory.getQuantity() > 0)
                .build();

    }
}
