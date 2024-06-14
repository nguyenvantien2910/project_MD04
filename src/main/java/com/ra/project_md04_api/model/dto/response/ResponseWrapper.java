package com.ra.project_md04_api.model.dto.response;

import com.ra.project_md04_api.constants.EHttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseWrapper<T> {
    EHttpStatus eHttpStatus;
    int statusCode;
    T data;
}
