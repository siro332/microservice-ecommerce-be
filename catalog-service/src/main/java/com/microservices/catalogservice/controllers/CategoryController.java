package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.exceptions.category.CategoryNotFoundException;
import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.services.ICategoryService;
import com.microservices.catalogservice.services.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalog/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{code}")
    public CategoryDto getCategoryByCode(@PathVariable String code) {
        return categoryService.getCategoryByCode(code)
                .orElseThrow(() -> new CategoryNotFoundException("Category with code [" + code + "] doesn't exist"));
    }

    @GetMapping("/discovery")
    public List<CategoryDto> getDiscoveryCategory(@RequestParam Integer number){
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        Collections.shuffle(categoryDtoList);
        return categoryDtoList.stream().limit(number).collect(Collectors.toList());
    }


    @PostMapping("/add")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PostMapping("/update/{code}")
    public Category updateCategory(@PathVariable String code, @RequestBody Category category) {
        return categoryService.update(code,category).orElseThrow(() -> new CategoryNotFoundException("Category with code [" + code + "] doesn't exist"));
    }
    @GetMapping("/names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<String> ids){
        List<String> list = this.categoryService.queryNameByIds(ids);
        if (list == null || list.size() <1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}