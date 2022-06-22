package com.example.searchservice.controllers;

import com.example.searchservice.services.SearchService;
import com.example.searchservice.models.Product;
import com.example.searchservice.search.ProductSearchDto;
import com.example.searchservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/search/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final SearchService searchService;

    @GetMapping("/add")
    public ResponseEntity<?> addProduct(@RequestParam("code") String code) {
        try {
            return ResponseEntity.ok(productService.buildProduct(code));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "all") String sortParam,
                                                              @RequestParam(defaultValue = "0") int sortDirection) {
        try {
            Page<Product> productPage;
            if (sortDirection == 0) {
                productPage = productService.getAllProducts(PageRequest.of(page, size, Sort.by(sortParam)));
            } else {
                productPage = productService.getAllProducts(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParam)));
            }
            Map<String, Object> response = getResponse(productPage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/by-category")
    public ResponseEntity<Map<String, Object>> getProductsByCategoryCode(@RequestParam String categoryCode,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "3") int size,
                                                                         @RequestParam(defaultValue = "all") String sortParam,
                                                                         @RequestParam(defaultValue = "0") int sortDirection) {
        try {
            Page<Product> productPage;
            if (sortDirection == 0) {
                productPage = productService.getProductByCategoryCode(categoryCode, PageRequest.of(page, size, Sort.by(sortParam)));
            } else {
                productPage = productService.getProductByCategoryCode(categoryCode, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParam)));
            }
            Map<String, Object> response = getResponse(productPage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> getProductContains(
            @RequestBody ProductSearchDto productSearch
    ) {
        try {
            Page<Product> productPage;
            productSearch.getProductSearch().setSearchTerm("*" + productSearch.getProductSearch().getSearchTerm() + "*");
            productPage = searchService.searchProduct(productSearch);
            Map<String, Object> response = getResponse(productPage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> getResponse(Page<Product> productPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return response;
    }
}

