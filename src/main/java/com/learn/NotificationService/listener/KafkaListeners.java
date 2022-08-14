package com.learn.NotificationService.listener;

import com.learn.NotificationService.service.NotificationConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class KafkaListeners {
    private final NotificationConsumerService notificationConsumerService;

    public KafkaListeners(NotificationConsumerService notificationConsumerService) {
        this.notificationConsumerService = notificationConsumerService;
    }

    @KafkaListener(topics = "notification.send_sms", groupId = "id ")
    void Listener(String data){
        log.info("Listener Recieved : {}", data);
        notificationConsumerService.consumeMessage(Integer.valueOf(data));
    }
}
