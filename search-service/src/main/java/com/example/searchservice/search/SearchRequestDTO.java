package com.example.searchservice.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequestDTO extends PagedRequestDTO {
    private List<String> fields;
    private String searchTerm;
    private String sortBy;
    private SortOrder order;

}
