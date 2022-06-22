package com.microservices.favoriteservice.controller;

import com.microservices.favoriteservice.models.Favorite;
import com.microservices.favoriteservice.models.FavoriteItem;
import com.microservices.favoriteservice.models.dto.FavoriteDto;
import com.microservices.favoriteservice.services.FavoriteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteServiceImpl cartService;

    private final RedisTemplate<String, Favorite> redisTemplate;

    @GetMapping("/user")
        public ResponseEntity<?> userCart(JwtAuthenticationToken authentication){
        String userCode = authentication.getName();
        FavoriteDto favoriteDto = cartService.getUserCart(userCode);
        return ResponseEntity.ok(favoriteDto);
    }

    @PostMapping("/addToFavorite")
    public ResponseEntity<?> addToFavorite(JwtAuthenticationToken authentication, @RequestBody FavoriteItem favoriteItem){
        try{
            String userCode = authentication.getName();
            Favorite favorite;
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                favorite = new Favorite();
                favorite.setUserCode(userCode);
                favorite.setFavoriteItemList(new ArrayList<>());
            }else {
                favorite = redisTemplate.opsForValue().get(userCode);
                assert favorite != null;
                Optional<FavoriteItem> optionalCartItem = favorite.getFavoriteItemList().stream().filter(favoriteItem1 -> Objects.equals(favoriteItem1.getProductCode(), favoriteItem.getProductCode())).findFirst();
                optionalCartItem.ifPresent(item -> favorite.getFavoriteItemList().remove(item));
            }
            favorite.getFavoriteItemList().add(favoriteItem);
            redisTemplate.opsForValue().set(userCode, favorite);
            return ResponseEntity.ok(redisTemplate.opsForValue().get(userCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error adding to cart: "+ e.getMessage());
        }
    }
    @PostMapping("/deleteItem")
    public ResponseEntity<?> deleteItem(JwtAuthenticationToken authentication,@RequestBody FavoriteItem favoriteItem){
        try{
            String userCode = authentication.getName();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                Favorite favorite = new Favorite();
                favorite.setUserCode(userCode);
                favorite.setFavoriteItemList(new ArrayList<>());
                favorite.getFavoriteItemList().remove(favoriteItem);
                redisTemplate.opsForValue().set(userCode, favorite);
            }else {
                Favorite favorite = redisTemplate.opsForValue().get(userCode);
                assert favorite != null;
                favorite.getFavoriteItemList().remove(favoriteItem);
                redisTemplate.opsForValue().set(userCode, favorite);
            }
            return ResponseEntity.ok(redisTemplate.opsForValue().get(userCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error deleting item from cart: "+ e.getMessage());
        }
    }
    @PostMapping("/clearFavorite")
    public ResponseEntity<?> clearCart(JwtAuthenticationToken authentication){
        try{
            String userCode = authentication.getName();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                Favorite favorite = new Favorite();
                favorite.setUserCode(userCode);
                favorite.setFavoriteItemList(new ArrayList<>());
                redisTemplate.opsForValue().set(userCode, favorite);
            }else {
                Favorite favorite = redisTemplate.opsForValue().get(userCode);
                assert favorite != null;
                favorite.setFavoriteItemList(new ArrayList<>());
                redisTemplate.opsForValue().set(userCode, favorite);
            }
            return ResponseEntity.ok(FavoriteDto.builder().userCode(userCode).favoriteItemDtos(new ArrayList<>()).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error clearing cart: "+ e.getMessage());
        }
    }
}
