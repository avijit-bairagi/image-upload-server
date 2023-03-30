package com.ovi.EnkaizenTest.common;

import lombok.Data;

@Data
public class ResponseBody<T> {

    private String code;

    private String message;

    private T data;

    public ResponseBody(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseBody() {
    }
}
