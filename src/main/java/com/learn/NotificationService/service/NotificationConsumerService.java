package com.learn.NotificationService.service;

import com.learn.NotificationService.model.ElasticSms;
import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.repository.ElasticSearchRepository;
import com.learn.NotificationService.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Component
public class NotificationConsumerService {

    NotificationService notificationService;
    RestTemplate restTemplate;

    RedisRepository redisRepository;

    ElasticSearchService elasticSearchService;

    public NotificationConsumerService(NotificationService notificationService,
                                       RestTemplate restTemplate, RedisRepository redisRepository,
                                       ElasticSearchService elasticSearchService){
        this.notificationService = notificationService;
        this.restTemplate = restTemplate;
        this.redisRepository = redisRepository;
        this.elasticSearchService = elasticSearchService;
    }
    @Cacheable("blacklist")
    public void consumeMessage(Integer request_id){
        SmsRequestDetails smsRequestDetails = notificationService.getSmsDetails(request_id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json; charset=UTF-8");
        HttpEntity<Object> request = new HttpEntity<>(smsRequestDetails, httpHeaders);
        log.info("SMS to be delivered : {}", smsRequestDetails);

        //check if number present in redis
        if(!Objects.equals(redisRepository.check(smsRequestDetails.getPhoneNumber()),null))
            log.info("the phone number is blacklisted");

        String response="";
        try {
            log.info("sending sms via third party api");
            response = restTemplate.postForObject("https://api.imiconnect.in/resources/v1/messaging", request, String.class);

        } catch(Exception e) {
            log.error("Sms Could not be delivered. Reason : {}", ExceptionUtils.getStackTrace(e));
        }
        ElasticSms elasticSms = new ElasticSms(smsRequestDetails);
        log.info("elastic sms is: {}", elasticSms);
        elasticSearchService.save(elasticSms);

        log.info("got response from 3rd party api: {}", response);

        //update status of message

        //index the details in elasticsearch
    }

}
