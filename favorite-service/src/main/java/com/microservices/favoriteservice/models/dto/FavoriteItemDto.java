package com.microservices.favoriteservice.models.dto;

import com.microservices.favoriteservice.models.catalog.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteItemDto {
    private ProductDto productDto;
}
