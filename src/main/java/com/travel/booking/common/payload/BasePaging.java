package com.travel.booking.common.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePaging<T> {
    private List<T> data;
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalData;
}
