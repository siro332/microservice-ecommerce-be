package com.microservices.inventoryservice.repositories;

import com.microservices.inventoryservice.entities.ProductInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Long> {
    List<ProductInventory> findByProductCode(String productCode);
    ProductInventory findBySku(String code);
    @Query(nativeQuery = true,value = "SELECT product_code" +
            "                    FROM" +
            "                     product_inventories join stock on product_inventories.id = stock.product_inventory_id" +
            "                    WHERE product_code IN :productCode " +
            "                    GROUP BY" +
            "                        product_code" +
            "                    ORDER BY" +
            "                      sum(stock.units_sold) DESC ")
    List<String> findTopSaleProduct(List<String> productCode);
}
