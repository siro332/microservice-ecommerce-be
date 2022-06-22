package com.microservices.favoriteservice.models;

import com.microservices.favoriteservice.models.dto.CartItemDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("orders")
@Data
public class Order {
    @Id
    private String id;
    private String code;
    private String name;
    private String address;
    private String zipCode;
    private String phoneNumber;
    private String email;
    private String note;
    private String paymentMethod;
    private boolean isPaid;
    private String userCode;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private List<CartItemDto> cartItem;
    private Double totals;
}
