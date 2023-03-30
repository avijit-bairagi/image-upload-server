package com.ovi.EnkaizenTest.common;

public enum ImageStatus {

    UPLOAD_SUCCESS(1),
    UPLOAD_FAILED(-1),
    RESIZE_SUCCESS(2),
    RESIZED_FAILED(2);

    private final int status;

    ImageStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
