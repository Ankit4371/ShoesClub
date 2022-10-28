package com.example.InventoryService.service;

import com.example.InventoryService.dao.InventoryDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryDAO inventoryDAO;

    @Transactional(readOnly = true)
    public boolean hasStocks(String skuCode){
        log.info("Stocks Checking {}",skuCode);
        return inventoryDAO.findBySkuCode(skuCode).isPresent();
    }
}
