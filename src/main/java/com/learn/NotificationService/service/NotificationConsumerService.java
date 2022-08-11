package com.learn.NotificationService.service;

import com.learn.NotificationService.model.entity.SmsRequestDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumerService {

    NotificationService notificationService;

    public NotificationConsumerService(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    public void consumeMessage(Integer request_id){
        SmsRequestDetails smsRequestDetails = notificationService.getSmsDetails(request_id);

        log.info("SMS to be delivered : {}", smsRequestDetails);

        //check if number present in redis

        //call third party api

        //update status of message

        //index the details in elasticsearch
    }

}
