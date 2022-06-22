package com.example.searchservice.services;

import com.example.searchservice.models.Product;
import com.example.searchservice.search.ProductSearchDto;
import com.example.searchservice.search.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final RestHighLevelClient client;

    public Page<Product> searchProduct(ProductSearchDto productSearchDto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                "product",
                productSearchDto.getProductSearch(),
                productSearchDto.getOptionalSearchRequestDto(),
                productSearchDto.getOptionalRangeSearchRequestDto()
        );

        return searchInternal(request);
    }

    private Page<Product> searchInternal(final SearchRequest request) {
        if (request == null) {
            log.error("Failed to build search request");
            return Page.empty();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[] searchHits = response.getHits().getHits();
            if (response.getHits().getTotalHits().value == 0){
                return Page.empty();
            }
            final List<Product> products = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                products.add(
                        MAPPER.readValue(hit.getSourceAsString(), Product.class)
                );
            }
            Page<Product> productPage;
            productPage = new PageImpl<>(products, PageRequest.of(0, products.size()),response.getHits().getTotalHits().value);
            return productPage;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Page.empty();
        }
    }

}
