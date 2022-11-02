package com.example.ProductService;

import com.example.ProductService.dao.ProductDAO;
import com.example.ProductService.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	// Integration Tests
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.2"));


	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper; // json to pojo or pojo to json

	@Autowired
	private ProductDAO productDAO;

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest dummy = getProductRequest();
		String dummyString = objectMapper.writeValueAsString(dummy);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(dummyString))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1,productDAO.findAll().size());
	}

	// TODO : tests
//	void shouldGetAllProducts(){
//
//	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Dummy Product")
				.description("Dummy Description")
				.price(BigDecimal.valueOf(1009))
				.build();
	}

}
