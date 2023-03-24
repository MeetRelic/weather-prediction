package com.weather.prediction.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpApiResponse<T> {
    private boolean success;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String errorMessage;

    public HttpApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

}
