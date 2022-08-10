package com.learn.NotificationService.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class kafkaTopicConfig {

public NewTopic sendSmsTopic(){
        return TopicBuilder.name("notification.send_sms").build();
    }
}
