package com.microservices.favoriteservice.services;

import com.microservices.favoriteservice.common.Utils;
import com.microservices.favoriteservice.models.Order;
import com.microservices.favoriteservice.models.dto.CartDto;
import com.microservices.favoriteservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    public Order createOrder(Order order){
        UUID uuid =UUID.randomUUID();
        order.setCode("OD"+ Utils.uuidToBase64(uuid));
        return orderRepository.save(order);
    }
    public Order getByCode(String code){
        return orderRepository.findByCode(code);
    };

    public Order changeStatus(String code, String status){
        Order order = orderRepository.findByCode(code);
        order.setStatus(status);
        return orderRepository.save(order);
    }
    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }
    public List<Order> getAllByUserCode(String userCode){
        return orderRepository.findByUserCode(userCode);
    }
}
