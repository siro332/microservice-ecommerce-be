package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.MediaDto;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Product;
import com.microservices.catalogservice.models.pojo.ProductPojo;
import com.microservices.catalogservice.repositories.CategoryRepository;
import com.microservices.catalogservice.repositories.ProductRepository;
import com.microservices.catalogservice.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryServiceImpl categoryService;
    private final MediaServiceClientImpl mediaServiceClient;

    private final DtoConverter dtoConverter;
    @Override
    public Product createProduct(ProductPojo form) {
        try {
            Set<Category> categories = categoryService.getListCategoryByCode(form.getCategoryCodeSet());
            UUID uuid = UUID.randomUUID();
            Product newProduct = Product.builder()
                    .name(form.getName())
                    .code("P" + Utils.uuidToBase64(uuid))
                    .slug(form.getSlug())
                    .isActive(form.getIsActive())
                    .createdAt(new Date())
                    .description(form.getDescription())
                    .userCode(form.getUserCode())
                    .categories(categories).build();
            log.info(newProduct.toString());
            return productRepository.save(newProduct);
        } catch (Exception e) {
            log.error("Error while creating product");
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Product updateProduct(ProductPojo form) {
        try {
            Set<Category> categories = categoryService.getListCategoryByCode(form.getCategoryCodeSet());
            Optional<Product> optionalProduct = productRepository.findByCode(form.getCode());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setCode(form.getCode());
                product.setCategories(categories);
                product.setUpdatedAt(new Date());
                product.setIsActive(form.getIsActive());
                product.setName(form.getName());
                product.setSlug(form.getSlug());
                product.setDescription(form.getDescription());
                return productRepository.save(product);
            }else{
                log.error("Product with code: "+form.getCode()+" not found!");
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("Error while creating product");
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Page<ProductDto> getUserProducts(String userCode, Pageable paging) {
        Page<Product> productPage = productRepository.findByUserCode(userCode,paging);

        return productPage.map(product -> {
            List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(product.getCode()).get();
            ProductDto productDto = dtoConverter.productEntityToDto(product);
            productDto.setMediaList(mediaList);
            return productDto;
        });
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable paging) {
        Page<Product> productPage = productRepository.findAll(paging);

        return productPage.map(product -> {
            List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(product.getCode()).get();
            ProductDto productDto = dtoConverter.productEntityToDto(product);
            productDto.setMediaList(mediaList);
            return productDto;
        });
    }

    @Override
    public Page<ProductDto> getProductContain(Pageable pageable, Specification<Product> spec) {
        Page<Product> productPage = productRepository.findAll(spec,pageable);

        return productPage.map(product -> {
            List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(product.getCode()).get();
            ProductDto productDto = dtoConverter.productEntityToDto(product);
            productDto.setMediaList(mediaList);
            return productDto;
        });
    }

    @Override
    public Optional<ProductDto> getProductByCode(String code) {
       Optional<Product> optionalProduct = productRepository.findByCode(code);
       if (optionalProduct.isPresent()) {
           log.info("Fetching media for product_code: " + code);
               List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(code).get();
               log.info("List media: " + mediaList);
               ProductDto productDto = dtoConverter.productEntityToDto(optionalProduct.get());
               productDto.setMediaList(mediaList);
               return Optional.of(productDto);
       }
       return Optional.empty();
    }

    @Override
    public void deleteProductByCode(String code) {

    }

    @Override
    public Page<ProductDto> getProductByCategoryCode(String categoryCode, Pageable paging) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByCode(categoryCode);
            if (optionalCategory.isPresent()){
                Page<Product> productPage = productRepository.findByCategoriesContains(optionalCategory.get(),paging);

                return productPage.map(product -> {
                    List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(product.getCode()).get();
                    ProductDto productDto = dtoConverter.productEntityToDto(product);
                    productDto.setMediaList(mediaList);
                    return productDto;
                });
            }
        }catch (Exception e){
            log.error("Error finding category with code: "+categoryCode);
            return Page.empty();
        }
        return null;
    }
}