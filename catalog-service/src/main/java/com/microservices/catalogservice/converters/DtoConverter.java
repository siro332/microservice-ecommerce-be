package com.microservices.catalogservice.converters;

import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoConverter {
    private final ModelMapper modelMapper;
    public CategoryDto categoryEntityToDto(Category category){
     return modelMapper.map(category, CategoryDto.class);
    }

    public ProductDto productEntityToDto(Product product){
        return modelMapper.map(product,ProductDto.class);
    }
}
