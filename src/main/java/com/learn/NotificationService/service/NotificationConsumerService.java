package com.learn.NotificationService.service;

import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Component
public class NotificationConsumerService {

    NotificationService notificationService;
    RestTemplate restTemplate;

    RedisRepository redisRepository;

    public NotificationConsumerService(NotificationService notificationService, RestTemplate restTemplate, RedisRepository redisRepository){
        this.notificationService = notificationService;
        this.restTemplate = restTemplate;
        this.redisRepository = redisRepository;
    }
    @Cacheable("blacklist")
    public void consumeMessage(Integer request_id){
        SmsRequestDetails smsRequestDetails = notificationService.getSmsDetails(request_id);

        log.info("SMS to be delivered : {}", smsRequestDetails);

        //check if number present in redis
        if(!Objects.equals(redisRepository.check(smsRequestDetails.getPhoneNumber()),null))
            log.info("the phone number is blacklisted");

//        restTemplate.postForEntity("https://api.imiconnect.in/resources/v1/messaging", )

        //update status of message

        //index the details in elasticsearch
    }

}
