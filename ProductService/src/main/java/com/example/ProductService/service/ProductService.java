package com.example.ProductService.service;

import com.example.ProductService.dao.ProductDAO;
import com.example.ProductService.dto.ProductRequest;
import com.example.ProductService.dto.ProductResponse;
import com.example.ProductService.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductDAO productDAO;

    // no need to write @Autowired after Spring 4.3
    // constructor injection handled by lombok
//    public ProductService(ProductDAO productDAO){
//        this.productDAO = productDAO;
//    }

    public void createProduct(ProductRequest productRequest){
        // Builder Design Pattern
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productDAO.save(product);
        log.info("Product {} is saved to db",product.getId());
    }

    public List<ProductResponse> getAll() {
        log.info("All products fetched");
        List<Product> productList = productDAO.findAll();
        //Stream api
        return productList.stream().map(product -> mapToProductResponse(product)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
