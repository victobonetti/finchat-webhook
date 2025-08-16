package br.com.kod3.models.util;

import java.util.List;

public record PaginatedResponse<T>(
    List<T> data, Integer pageIndex, Integer pageSize, Long totalCount) {}
