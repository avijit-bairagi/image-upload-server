package com.ovi.EnkaizenTest.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
