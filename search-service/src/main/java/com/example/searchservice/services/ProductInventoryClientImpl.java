package com.example.searchservice.services;

import com.example.searchservice.models.product_inventory.ProductInventory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductInventoryClientImpl {
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "productInventoryServiceClient", fallbackMethod = "getDefaultProductInventoryByCode")
    public Optional<ProductInventory> getProductInventoryByCode(String code)
    {
        ResponseEntity<ProductInventory> itemResponseEntity =
                restTemplate.getForEntity("http://inventory-service/api/inventories/"+code,ProductInventory.class);
        if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
            return Optional.of(itemResponseEntity.getBody());
        } else {
            log.error("Unable to get product inventory: " + code + ", StatusCode: " + itemResponseEntity.getStatusCode());
            return Optional.empty();
        }
    }

    ProductInventory getDefaultProductInventoryByCode(Exception e) {
        log.info("Returning default ProductInventoryByCode ");
        ProductInventory productInventory= new ProductInventory();
        return productInventory;
    }
    @CircuitBreaker(name = "productInventoryServiceClient", fallbackMethod = "getDefaultProductInventoryByProductCode")
    public List<ProductInventory> getProductInventoryByProductCode(String productCode)
    {
        ResponseEntity<List<ProductInventory>> itemResponseEntity =
                restTemplate.exchange(
                        "http://inventory-service/api/inventories/product/"+productCode,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
            return itemResponseEntity.getBody();
        } else {
            log.error("Unable to get media level for product_code: " + productCode + ", StatusCode: " + itemResponseEntity.getStatusCode());
            return null;
        }
    }

    Optional<List<ProductInventory>> getDefaultProductInventoryByProductCode(Exception e) {
        log.info("Returning default ProductInventoryByCode for productCode: "+"productCode");
        List<ProductInventory> productInventoryList= new ArrayList<>();
        return Optional.of(productInventoryList);
    }
}
