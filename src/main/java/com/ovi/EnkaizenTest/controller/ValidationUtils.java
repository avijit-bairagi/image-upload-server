package com.ovi.EnkaizenTest.controller;

import com.ovi.EnkaizenTest.common.Constants;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ValidationUtils {

    public static String validate(List<MultipartFile> images) {

        for (MultipartFile file : images) {

            if (file == null) {
                return "Please upload valid formatted file(s).";
            }

            String fileName = file.getOriginalFilename();

            if (fileName == null) {
                return "Please upload valid formatted file(s).";
            }

            int index = fileName.lastIndexOf(".");

            if (index == -1) {
                return "Please upload valid formatted file(s).";
            }

            String format = file.getOriginalFilename().substring(index + 1);

            if (!Constants.imageFileFormats.contains(format.toLowerCase())) {
                return "Please upload valid formatted file(s).";
            }
        }

        return null;
    }
}
