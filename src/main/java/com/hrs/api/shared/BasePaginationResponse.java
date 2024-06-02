package com.hrs.api.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasePaginationResponse<T> {
    private Boolean success;
    private String message;
    private Integer total;
    private List<T> data;

    public static<T> BasePaginationResponse success(List<T> data, Integer total) {
        return BasePaginationResponse.<T>builder()
                .success(Boolean.TRUE)
                .total(total)
                .data(data)
                .build();
    }

    public static<T> BasePaginationResponse success(List<T> data) {
        return BasePaginationResponse.<T>builder()
                .success(Boolean.TRUE)
                .data(data)
                .build();
    }

    public static <T> BasePaginationResponse<T> error(String message) {
        return BasePaginationResponse.<T>builder()
                .success(Boolean.FALSE)
                .message(message)
                .build();
    }
}
