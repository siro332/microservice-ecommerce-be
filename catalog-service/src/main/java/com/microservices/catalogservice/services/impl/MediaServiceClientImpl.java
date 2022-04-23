package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.models.dtos.MediaDto;
import com.microservices.catalogservice.models.responses.ProductMediaResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceClientImpl {
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "mediaServiceClient", fallbackMethod = "getDefaultMediaByCode")
    public Optional<List<MediaDto>> getMediaByProductCode(String productCode)
    {
        ResponseEntity<ProductMediaResponse> itemResponseEntity =
                restTemplate.getForEntity("http://media-service/api/media/{code}",
                        ProductMediaResponse.class,
                        productCode);
        if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
            return Optional.of(itemResponseEntity.getBody().getMediaList());
        } else {
            log.error("Unable to get media level for product_code: " + productCode + ", StatusCode: " + itemResponseEntity.getStatusCode());
            return Optional.empty();
        }
    }

    Optional<List<MediaDto>> getDefaultMediaByCode(Exception e) {
        log.info("Returning default ProductMediaByCode for productCode: "+"productCode");
        List<MediaDto> media= new ArrayList<>();
        media.add(MediaDto.builder()
                        .code("productCode")
                        .createdAt(new Date())
                        .altText("No Image")
                        .imgUrl("")
                        .isFeature(true)
                .build());
        return Optional.of(media);
    }
}
