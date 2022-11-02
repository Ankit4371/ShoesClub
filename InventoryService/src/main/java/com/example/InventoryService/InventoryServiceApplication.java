package com.example.InventoryService;

import com.example.InventoryService.dao.InventoryDAO;
import com.example.InventoryService.model.Inventory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
@EnableEurekaClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner loadData(InventoryDAO inventoryDAO){
//		return args -> {
//			Inventory inventory = new Inventory();
//			inventory.setSkuCode("abc1");
//			inventory.setQuantity(100);
//
//			Inventory inventory1 = new Inventory();
//			inventory1.setSkuCode("abc2");
//			inventory1.setQuantity(0);
//
//			inventoryDAO.save(inventory);
//			inventoryDAO.save(inventory1);
//
//		};
//}

}
