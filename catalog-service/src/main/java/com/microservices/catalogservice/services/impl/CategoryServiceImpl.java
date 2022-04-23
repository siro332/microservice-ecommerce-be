package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.dtos.MediaDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.repositories.CategoryRepository;
import com.microservices.catalogservice.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final DtoConverter dtoConverter;

    private final MediaServiceClientImpl mediaServiceClient;

    @Override
    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> allSubCategories = categoryRepository.findAllByParentCodeIsNotNull()
                .stream().map(dtoConverter::categoryEntityToDto)
                .collect(Collectors.toList());
        List<CategoryDto> rootCategories = categoryRepository.findAllByParentCodeIsNull()
                .stream().map(dtoConverter::categoryEntityToDto)
                .collect(Collectors.toList());
        Map<String, List<CategoryDto>> groupedSubCat = allSubCategories.stream()
                .collect(Collectors.groupingBy(CategoryDto::getParentCode));
        for (CategoryDto category : rootCategories
        ) {
            List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(category.getCode()).get();
            log.info("List media: " + mediaList);
            findSubCat(category,groupedSubCat);
            category.setMediaList(mediaList);
        }
        return rootCategories;
    }

    @Override
    public Optional<CategoryDto> getCategoryByCode(String code) {
        Optional<Category> optionalCategory = categoryRepository.findByCode(code);
        if (optionalCategory.isPresent()){
            CategoryDto category = dtoConverter.categoryEntityToDto(optionalCategory.get());
            List<CategoryDto> allSubCategories = categoryRepository.findAllByParentCodeIsNotNull()
                    .stream().map(dtoConverter::categoryEntityToDto)
                    .collect(Collectors.toList());
            Map<String, List<CategoryDto>> groupedSubCat = allSubCategories.stream()
                    .collect(Collectors.groupingBy(CategoryDto::getParentCode));
            List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(code).get();
            log.info("List media: " + mediaList);
            CategoryDto resultCategory = findSubCat(category,groupedSubCat);
            resultCategory.setMediaList(mediaList);
            return Optional.of(resultCategory);
        }
        return Optional.empty();
    }

    @Override
    public Set<Category> getListCategoryByCode(Set<String> categoryCodeList) {
        return categoryRepository.findByCodeIn(categoryCodeList);
    }

    @Override
    public Category addCategory(Category category) {
        UUID uuid =UUID.randomUUID();
        category.setCreatedAt(new Date());
        category.setCode("CT"+ Utils.uuidToBase64(uuid));
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> update(String code, Category newCategory) {
        Optional<Category> optionalCategory = categoryRepository.findByCode(code);
        if (optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setName(newCategory.getName());
            category.setIsActive(newCategory.getIsActive());
            category.setSlug(newCategory.getSlug());
            category.setParentCode(newCategory.getParentCode());
            category.setUpdatedAt(new Date());
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }

    private CategoryDto findSubCat(CategoryDto category, Map<String, List<CategoryDto>> result) {
        if (result.containsKey(category.getCode())) {
            List<CategoryDto> tempSubCats = result.get(category.getCode());
            List<CategoryDto> subCats = new ArrayList<>();
            for (CategoryDto subCat : tempSubCats
            ) {
                subCats.add(findSubCat(subCat, result));
            }
            category.setSubCategories(subCats);
            return category;
        }
        return category;
    }
}
