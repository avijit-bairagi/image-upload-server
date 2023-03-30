package com.ovi.EnkaizenTest.common;

public enum ResponseStatus {

    SUCCESS("000"),
    INVALID_FILE("001"),
    UNKNOWN("999");

    private final String code;

    ResponseStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
