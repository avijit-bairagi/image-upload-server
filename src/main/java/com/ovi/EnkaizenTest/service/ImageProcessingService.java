package com.ovi.EnkaizenTest.service;

import com.ovi.EnkaizenTest.common.Constants;
import com.ovi.EnkaizenTest.common.ImageStatus;
import com.ovi.EnkaizenTest.common.ResponseBody;
import com.ovi.EnkaizenTest.common.ResponseStatus;
import com.ovi.EnkaizenTest.common.utils.ImageUtils;
import com.ovi.EnkaizenTest.dao.entity.ImageEntity;
import com.ovi.EnkaizenTest.dao.repository.ImageRepository;
import com.ovi.EnkaizenTest.mq.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ImageProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(ImageProcessingService.class);

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RabbitMQProducer producer;

    private final ImageRepository imageRepository;

    private final String uploadDir;

    public ImageProcessingService(SimpMessagingTemplate simpMessagingTemplate,
                                  RabbitMQProducer producer,
                                  ImageRepository imageRepository,
                                  @Value("${image.path}") String uploadDir) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.producer = producer;
        this.imageRepository = imageRepository;
        this.uploadDir = uploadDir;
    }

    public void publish(List<MultipartFile> images) {

        Long parentId = System.currentTimeMillis();

        AtomicInteger integer = new AtomicInteger(0);

        images.forEach(file -> {

            int fileStatus = ImageStatus.UPLOAD_SUCCESS.getStatus();

            int index = file.getOriginalFilename().lastIndexOf(".");

            String format = file.getOriginalFilename().substring(index + 1);

            String fileName = parentId + "_" + integer.addAndGet(1) + "." + format;
            File dest = new File(uploadDir + fileName);

            try {
                file.transferTo(dest);
            } catch (IOException e) {
                logger.error("Error occurred while saving image file.", e);
                fileStatus = ImageStatus.UPLOAD_FAILED.getStatus();
            }

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setParentId(parentId);
            imageEntity.setOriginalFileName(fileName);
            imageEntity.setStatus(fileStatus);
            imageEntity.setInsertedAt(LocalDateTime.now());
            imageEntity.setUpdatedAt(LocalDateTime.now());

            imageRepository.save(imageEntity);
        });

        producer.publishTask(parentId);

        sendNotification("Images uploaded successfully. TaskId: " + parentId + ". File uploaded at: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").format(new Date()));
    }

    public void resize(Long taskId) {

        imageRepository.findByParentId(taskId).forEach(imageEntity -> {

            int fileStatus = ImageStatus.RESIZE_SUCCESS.getStatus();

            try {
                File file = new File(uploadDir + imageEntity.getOriginalFileName());

                int index = imageEntity.getOriginalFileName().lastIndexOf(".");

                String fileName = imageEntity.getOriginalFileName().substring(0, index);
                String format = imageEntity.getOriginalFileName().substring(index + 1);

                fileName = fileName + "_resized." + format;

                imageEntity.setThumbnailFileName(fileName);

                BufferedImage bi = ImageUtils.resizeImage(ImageIO.read(file), Constants.ImageResize.width, Constants.ImageResize.height);
                File outputfile = new File(uploadDir + fileName);
                ImageIO.write(bi, format, outputfile);

            } catch (Exception e) {
                fileStatus = ImageStatus.RESIZED_FAILED.getStatus();
                logger.error("Error occurred while resizing image file.", e);
            }

            imageEntity.setStatus(fileStatus);
            imageEntity.setUpdatedAt(LocalDateTime.now());

            imageRepository.save(imageEntity);

        });

        //todo: this below sleep code is temporary to visualize client and server communication.
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        sendNotification("Images resized successfully. TaskId: " + taskId + ". Images resized at: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").format(new Date()));
    }

    private void sendNotification(String message) {
        simpMessagingTemplate.convertAndSend("/topic/push-messages", new ResponseBody<>(ResponseStatus.SUCCESS.getCode(), message));
    }
}
