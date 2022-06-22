package com.example.searchservice.configs;

import com.example.searchservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListeners {
    private final ProductService productService;

    @KafkaListener(topics = "product", groupId = "test")
    void listener(String data) {
        log.info(data);
        productService.addKafkaData(data);
    }
}
