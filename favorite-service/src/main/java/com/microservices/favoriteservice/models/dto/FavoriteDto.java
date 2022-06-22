package com.microservices.favoriteservice.models.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteDto {
    private String userCode;
    private List<FavoriteItemDto> favoriteItemDtos;
}
