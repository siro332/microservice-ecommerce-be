package com.microservices.catalogservice.services;

import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.entities.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ICategoryService {
    public List<CategoryDto> getAllCategories();
    public Optional<CategoryDto> getCategoryByCode(String code);
    public Set<Category> getListCategoryByCode(Set<String> categoryCodeList);
    Category addCategory(Category category);

    Optional<Category> update(String code, Category category);

    List<String> queryNameByIds(List<String> ids);
}
