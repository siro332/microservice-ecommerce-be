package com.microservices.favoriteservice.repository;

import com.microservices.favoriteservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> findAll();
    List<Order> findByUserCode(String code);
    Order findByCode(String code);
}
