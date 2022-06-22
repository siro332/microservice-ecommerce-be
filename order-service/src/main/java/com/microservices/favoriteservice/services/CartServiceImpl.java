package com.microservices.favoriteservice.services;

import com.microservices.favoriteservice.models.Cart;
import com.microservices.favoriteservice.models.CartItem;
import com.microservices.favoriteservice.models.catalog.ProductDto;
import com.microservices.favoriteservice.models.dto.CartDto;
import com.microservices.favoriteservice.models.dto.CartItemDto;
import com.microservices.favoriteservice.models.product_inventory.ProductInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl {
    private final ProductClientImpl productClient;
    private final ProductInventoryClientImpl productInventoryClient;
    private final RedisTemplate<String, Cart> redisTemplate;


    public CartItemDto getDetailCartItem(CartItem cartItem){
        CartItemDto cartItemDto = new CartItemDto();
        Optional<ProductDto> productDto = productClient.getProductInventoryByCode(cartItem.getProductCode());
        if (productDto.isPresent()){
            cartItemDto.setProductDto(productDto.get());
        }
        Optional<ProductInventory> optionalProductInventory = productInventoryClient.getProductInventoryByCode(cartItem.getProductInventory());
        if (optionalProductInventory.isPresent()){
            cartItemDto.setInventoryItem(optionalProductInventory.get());
        }
        cartItemDto.setUnits(cartItem.getUnits());
        return cartItemDto;
    }
    public CartDto getUserCart(String userCode){
        CartDto cartDto = CartDto.builder().userCode(userCode).build();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
            Cart cart = new Cart();
            cart.setUserCode(userCode);
            cart.setCartItemList(new ArrayList<>());
            redisTemplate.opsForValue().set(userCode,cart);
            cartDto.setCartItemDtos(new ArrayList<>());
        }else {
            Cart cart = redisTemplate.opsForValue().get(userCode);
            List<CartItemDto> cartItemDtos = new ArrayList<>();
            for (CartItem cartItem: cart.getCartItemList()
                 ) {
                cartItemDtos.add(getDetailCartItem(cartItem));
            }
            cartDto.setCartItemDtos(cartItemDtos);
        }
        double sum = 0;
        for (CartItemDto cartItemDto: cartDto.getCartItemDtos()
             ) {
           sum += cartItemDto.getUnits() * cartItemDto.getInventoryItem().getRetailPrice();
        }
        cartDto.setTotals(sum);
        return cartDto;
    }
}
