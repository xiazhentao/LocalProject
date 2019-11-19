package com.example.localproject.domain;

import lombok.Data;

/**
 * @author xiazhengtao
 * @date 2019-07-25 20:44
 */
@Data
public class AppResponse<T> {

    private Integer code;

    private T data;

    private String message;
}
