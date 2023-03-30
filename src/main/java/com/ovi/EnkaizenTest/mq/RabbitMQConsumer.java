package com.ovi.EnkaizenTest.mq;

import com.ovi.EnkaizenTest.service.ImageProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private final ImageProcessingService imageProcessingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    public RabbitMQConsumer(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue}"})
    public void consume(Long taskId) {
        LOGGER.info(String.format("Received task -> %s", taskId));
        imageProcessingService.resize(taskId);
    }
}