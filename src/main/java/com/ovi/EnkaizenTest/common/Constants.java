package com.ovi.EnkaizenTest.common;

import java.util.Arrays;
import java.util.List;

public interface Constants {

    List<String> imageFileFormats = Arrays.asList("jpg", "jpeg", "png");

    interface ImageResize {

        int height = 200;
        int width = 200;
    }
}
