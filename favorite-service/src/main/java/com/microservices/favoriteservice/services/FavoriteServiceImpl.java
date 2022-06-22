package com.microservices.favoriteservice.services;

import com.microservices.favoriteservice.models.Favorite;
import com.microservices.favoriteservice.models.FavoriteItem;
import com.microservices.favoriteservice.models.catalog.ProductDto;
import com.microservices.favoriteservice.models.dto.FavoriteDto;
import com.microservices.favoriteservice.models.dto.FavoriteItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl {
    private final ProductClientImpl productClient;
    private final RedisTemplate<String, Favorite> redisTemplate;


    public FavoriteItemDto getDetailFavoriteItem(FavoriteItem favoriteItem){
        FavoriteItemDto favoriteItemDto = new FavoriteItemDto();
        Optional<ProductDto> productDto = productClient.getProductInventoryByCode(favoriteItem.getProductCode());
        productDto.ifPresent(favoriteItemDto::setProductDto);
        return favoriteItemDto;
    }
    public FavoriteDto getUserCart(String userCode){
        FavoriteDto favoriteDto = FavoriteDto.builder().userCode(userCode).build();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
            Favorite favorite = new Favorite();
            favorite.setUserCode(userCode);
            favorite.setFavoriteItemList(new ArrayList<>());
            redisTemplate.opsForValue().set(userCode, favorite);
            favoriteDto.setFavoriteItemDtos(new ArrayList<>());
        }else {
            Favorite favorite = redisTemplate.opsForValue().get(userCode);
            List<FavoriteItemDto> favoriteItemDtos = new ArrayList<>();
            assert favorite != null;
            for (FavoriteItem favoriteItem : favorite.getFavoriteItemList()
                 ) {
                favoriteItemDtos.add(getDetailFavoriteItem(favoriteItem));
            }
            favoriteDto.setFavoriteItemDtos(favoriteItemDtos);
        }
        return favoriteDto;
    }
}
