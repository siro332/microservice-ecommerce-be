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

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    private final ProductInventoryRepository productInventoryRepository;

    @GetMapping("/{productCode}")
    public ResponseEntity<ProductInventory> findInventoryByProductCode(@PathVariable("productCode") String productCode) {
        log.info("Finding inventory for product code :"+productCode);
        List<ProductInventory> productInventoryList = productInventoryRepository.findByProductCode(productCode);
        if(productInventoryList.size()!=0) {
            return new ResponseEntity(productInventoryList, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
