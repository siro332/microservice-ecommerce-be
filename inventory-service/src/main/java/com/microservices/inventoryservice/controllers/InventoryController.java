package com.microservices.inventoryservice.controllers;

import com.microservices.inventoryservice.entities.ProductInventory;
import com.microservices.inventoryservice.repositories.ProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    private final ProductInventoryRepository productInventoryRepository;

    @GetMapping("/product/{productCode}")
    public List<ProductInventory> findInventoryByProductCode(@PathVariable("productCode") String productCode) {
        log.info("Finding inventory for product code :"+productCode);
        List<ProductInventory> productInventoryList = productInventoryRepository.findByProductCode(productCode);
        if(productInventoryList.size()!=0) {
            return productInventoryList;
        } else {
            return Collections.emptyList();
        }
    }
    @GetMapping("/{code}")
    public ResponseEntity<ProductInventory> findInventoryByCode(@PathVariable("code") String code) {
        log.info("Finding inventory for code :"+code);
        ProductInventory productInventory = productInventoryRepository.findBySku(code);
            return new ResponseEntity(productInventory, HttpStatus.OK);
    }
}
