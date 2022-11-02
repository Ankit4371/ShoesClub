package com.example.InventoryService.dao;

import com.example.InventoryService.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryDAO extends JpaRepository<Inventory,Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCodeList);
}
