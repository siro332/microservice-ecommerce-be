package com.microservices.favoriteservice.models.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private String userCode;
    private List<CartItemDto> cartItemDtos;
    private Double totals;
}
