package com.microservices.inventoryservice.repositories;

import com.microservices.inventoryservice.entities.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Long> {
    List<ProductInventory> findByProductCode(String productCode);
}
