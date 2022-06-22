package com.microservices.favoriteservice.services;

import com.microservices.favoriteservice.models.catalog.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductClientImpl {
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "productServiceClient", fallbackMethod = "getDefaultProductByCode")
    public Optional<ProductDto> getProductInventoryByCode(String code)
    {
        ResponseEntity<ProductDto> itemResponseEntity =
                restTemplate.getForEntity("http://catalog-service/api/catalog/products/"+code,ProductDto.class);
        if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
            return Optional.of(itemResponseEntity.getBody());
        } else {
            log.error("Unable to get product inventory: " + code + ", StatusCode: " + itemResponseEntity.getStatusCode());
            return Optional.empty();
        }
    }

    ProductDto getDefaultProductByCode(Exception e) {
        log.info("Returning default ProductInventoryByCode ");
        ProductDto productDto= new ProductDto();
        return productDto;
    }
}
