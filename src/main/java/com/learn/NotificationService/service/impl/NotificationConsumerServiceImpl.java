package com.learn.NotificationService.service.impl;

import com.learn.NotificationService.model.ElasticSms;
import com.learn.NotificationService.model.Imi.ImiRequest;
import com.learn.NotificationService.model.Imi.ImiResponse;
import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.repository.RedisRepository;
import com.learn.NotificationService.service.NotificationConsumerService;
import com.learn.NotificationService.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class NotificationConsumerServiceImpl implements NotificationConsumerService {

    NotificationService notificationService;
    RestTemplate restTemplate;

    RedisRepository redisRepository;

    ElasticSearchServiceImpl elasticSearchService;

    public NotificationConsumerServiceImpl(NotificationService notificationService,
                                           RestTemplate restTemplate, RedisRepository redisRepository,
                                           ElasticSearchServiceImpl elasticSearchService){
        this.notificationService = notificationService;
        this.restTemplate = restTemplate;
        this.redisRepository = redisRepository;
        this.elasticSearchService = elasticSearchService;
    }
    @Override
    @Cacheable("blacklist")
    public void consumeMessage(Integer request_id){
        SmsRequestDetails smsRequestDetails = notificationService.getSmsDetails(request_id);
        log.info("SMS to be delivered : {}", smsRequestDetails);
        Object imiResponse = new ImiResponse();
        if(!Objects.equals(redisRepository.check(smsRequestDetails.getPhoneNumber()),null)) {
            log.info("the phone number {} is blacklisted", smsRequestDetails.getPhoneNumber());
        }
        else {
            try {
                log.info("sending sms via third party api");
                imiResponse = sendSmsNotification(smsRequestDetails);
            } catch (Exception e) {
                log.error("Sms Could not be delivered. Reason : {}", ExceptionUtils.getStackTrace(e));
            }
            ElasticSms elasticSms = new ElasticSms(smsRequestDetails);
            log.info("elastic sms is: {}", elasticSms);
            elasticSearchService.save(elasticSms);

            log.info("got response from 3rd party api: {}", imiResponse);
        }
    }

    private Object sendSmsNotification(SmsRequestDetails smsRequestDetails) {

        ImiRequest imiRequest = new ImiRequest();
        imiRequest.getChannels().getSms().setText(smsRequestDetails.getMessage() );
        imiRequest.getDestination().get(0).setMsisdn(Collections.singletonList(smsRequestDetails.getPhoneNumber()));
        imiRequest.getDestination().get(0).setCorrelationId("some_unique_id");

        String url = "https://api.imiconnect.in/resources/v1/messaging";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType((MediaType.APPLICATION_JSON));
        httpHeaders.set("key", "93ceffda-5941-11ea-9da9-025282c394f2");
        log.info("sending map : {}", imiRequest);
        ResponseEntity<Object> responseEntity;
        HttpEntity<ImiRequest> requestEntity = new HttpEntity<>(imiRequest, httpHeaders);
        responseEntity = restTemplate.exchange(url, HttpMethod.POST,requestEntity, Object.class);
        log.info("response we got is {}", responseEntity);
        return responseEntity.getBody();
    }

}
