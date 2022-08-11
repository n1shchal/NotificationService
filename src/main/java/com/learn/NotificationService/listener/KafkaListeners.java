package com.learn.NotificationService.listener;

import com.learn.NotificationService.service.NotificationConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    private final NotificationConsumerService notificationConsumerService;

    public KafkaListeners(NotificationConsumerService notificationConsumerService) {
        this.notificationConsumerService = notificationConsumerService;
    }

    @KafkaListener(topics = "notification.send_sms", groupId = "id ")
    void Listener(String data){
        System.out.println("Listener Recieved " + data);
        notificationConsumerService.consumeMessage(Integer.valueOf(data));
    }
}
