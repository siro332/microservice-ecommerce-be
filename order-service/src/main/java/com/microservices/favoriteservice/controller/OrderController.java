package com.microservices.favoriteservice.controller;

import com.microservices.favoriteservice.models.Cart;
import com.microservices.favoriteservice.models.Order;
import com.microservices.favoriteservice.models.dto.CartDto;
import com.microservices.favoriteservice.services.CartServiceImpl;
import com.microservices.favoriteservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order/order")
@RequiredArgsConstructor
public class OrderController {
    private final CartServiceImpl cartService;
    private final OrderService orderService;
    private final CartController cartController;
    private final RedisTemplate<String, Cart> redisTemplate;
    @GetMapping("")
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrder());
    }


    @GetMapping("/user")
    public ResponseEntity<?> userCart(JwtAuthenticationToken authentication){
        String userCode = authentication.getName();
        return ResponseEntity.ok(orderService.getAllByUserCode(userCode));
    }
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(JwtAuthenticationToken authentication, @RequestBody Order order){
        try{
            CartDto cartDto = cartService.getUserCart(authentication.getName());
            order.setCartItem(cartDto.getCartItemDtos());
            order.setTotals(cartDto.getTotals());
            order.setUserCode(authentication.getName());
            Order newOrder = orderService.createOrder(order);
            cartController.clearCart(authentication);
            return ResponseEntity.ok(newOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating order: "+ e.getMessage());
        }
    }
    @PostMapping("/change-status/{code}")
    public ResponseEntity<?> changeStatus(@PathVariable String code, @RequestParam String status){
        try{
            return ResponseEntity.ok(orderService.changeStatus(code,status));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating order: "+ e.getMessage());
        }
    }
}
