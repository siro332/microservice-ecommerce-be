package com.microservices.favoriteservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItem {
    private String productCode;
    private String productInventory;
    private Integer units;
}
