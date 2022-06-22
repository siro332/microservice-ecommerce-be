package com.example.searchservice.search.util;

import com.example.searchservice.search.RangeSearchRequestDTO;
import com.example.searchservice.search.SearchRequestDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class SearchUtil {

    private SearchUtil() {
    }

    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final SearchRequestDTO dto) {
        try {
            final int page = dto.getPage();
            final int size = dto.getSize();
            final int from = page <= 0 ? 0 : page * size;

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .from(from)
                    .size(size)
                    .postFilter(getQueryBuilder(dto));

            if (dto.getSortBy() != null) {
                builder = builder.sort(
                        dto.getSortBy(),
                        dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC
                );
            }

            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final SearchRequestDTO dto,
                                                   final List<SearchRequestDTO> optionalSearchRequestDto,
                                                   final List<RangeSearchRequestDTO> rangeSearchRequestDto) {
        try {
            final int page = dto.getPage();
            final int size = dto.getSize();
            final int startFrom = page <= 0 ? 0 : page * size;

            final QueryBuilder searchQuery = getWildCardQueryBuilder(dto);
            List<QueryBuilder> optionalSearchQuery = new ArrayList<>();
            List<QueryBuilder> rangeSearchQuery = new ArrayList<>();
            for (SearchRequestDTO requestDTO: optionalSearchRequestDto
                 ) {
                optionalSearchQuery.add(getQueryBuilder(requestDTO));
            }
            for (RangeSearchRequestDTO requestDTO: rangeSearchRequestDto
            ) {
                rangeSearchQuery.add(getQueryBuilder(requestDTO.getField(), requestDTO.getFrom(), requestDTO.getTo()));
            }
            final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                    .must(searchQuery);

            for (QueryBuilder query: optionalSearchQuery
                 ) {
                boolQuery.must(query);
            }
            for (QueryBuilder query: rangeSearchQuery
            ) {
                boolQuery.must(query);
            }

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .from(startFrom)
                    .size(size)
                    .postFilter(boolQuery);

            if (dto.getSortBy() != null) {
                builder = builder.sort(
                        dto.getSortBy(),
                        dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC
                );
            }

            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static QueryBuilder getQueryBuilder(final SearchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        final List<String> fields = dto.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        if (fields.size() > 1) {
            final MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.AND);

            fields.forEach(queryBuilder::field);

            return queryBuilder;
        }

        return fields.stream()
                .findFirst()
                .map(field ->
                        QueryBuilders.matchQuery(field, dto.getSearchTerm())
                                .operator(Operator.AND))
                .orElse(null);
    }
    private static QueryBuilder getWildCardQueryBuilder(final SearchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        final List<String> fields = dto.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        return fields.stream()
                .findFirst()
                .map(field ->
                        QueryBuilders.wildcardQuery(field, dto.getSearchTerm()))
                .orElse(null);
    }

    private static QueryBuilder getQueryBuilder(final String field, final double start, final double end) {
        return QueryBuilders.rangeQuery(field).gte(start).lte(end);
    }
}
