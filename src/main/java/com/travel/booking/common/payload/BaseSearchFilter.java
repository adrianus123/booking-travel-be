package com.travel.booking.common.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseSearchFilter<T> {
    private T data;
    private String orderBy;
    private boolean isDescending;
    private Integer page;
    private Integer size;
}
