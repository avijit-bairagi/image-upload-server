package com.ovi.EnkaizenTest.controller;

import com.ovi.EnkaizenTest.common.ResponseBody;
import com.ovi.EnkaizenTest.common.ResponseStatus;
import com.ovi.EnkaizenTest.service.ImageProcessingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/upload")
@CrossOrigin(originPatterns = "*")
public class ImageUploadController {

    private final ImageProcessingService imageProcessingService;

    public ImageUploadController(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    @PostMapping(value = "/images")
    public ResponseBody<Object> upload(@RequestParam("images") List<MultipartFile> images) {

        String message = ValidationUtils.validate(images);

        if (message != null) {
            return new ResponseBody<>(ResponseStatus.INVALID_FILE.getCode(), message);
        }

        imageProcessingService.publish(images);

        return new ResponseBody<>(ResponseStatus.SUCCESS.getCode(), "Image upload tasks received successfully. You will be notified when tasks are done");
    }
}
