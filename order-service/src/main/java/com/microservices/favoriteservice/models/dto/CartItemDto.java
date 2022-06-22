package com.microservices.favoriteservice.models.dto;

import com.microservices.favoriteservice.models.catalog.ProductDto;
import com.microservices.favoriteservice.models.product_inventory.ProductInventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private ProductDto productDto;
    private ProductInventory inventoryItem;
    private Integer units;
}
