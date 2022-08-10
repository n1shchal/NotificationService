package com.learn.NotificationService.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "notification.send_sms", groupId = "id ")
    void Listener(String data){
        System.out.println("Listener Recieved " + data);
    }
}
