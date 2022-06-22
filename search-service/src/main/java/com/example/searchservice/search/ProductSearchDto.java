package com.example.searchservice.search;

import lombok.Data;

import java.util.List;

@Data
public class ProductSearchDto {
    private SearchRequestDTO productSearch;
    private List<SearchRequestDTO> optionalSearchRequestDto;
    private List<RangeSearchRequestDTO> optionalRangeSearchRequestDto;

}
