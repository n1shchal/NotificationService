package com.learn.NotificationService.service.impl;


import com.learn.NotificationService.model.*;
import com.learn.NotificationService.model.entity.Blacklist;
import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.repository.BlacklistRepository;
import com.learn.NotificationService.repository.RedisRepository;
import com.learn.NotificationService.repository.SmsRequestRepository;
import com.learn.NotificationService.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NotificationServiceImpl implements NotificationService {

    private final SmsRequestRepository smsRequestRepository;
    private final BlacklistRepository blacklistRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisRepository redisRepository;


    public NotificationServiceImpl(SmsRequestRepository smsRequestRepository,
                                   BlacklistRepository blacklistRepository,
                                   KafkaTemplate<String, String> kafkaTemplate, RedisRepository redisRepository) {
        this.smsRequestRepository = smsRequestRepository;
        this.blacklistRepository = blacklistRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.redisRepository = redisRepository;
    }

    @Override
    public SmsSuccess sendAndSaveSms(SmsRequest smsRequest) throws BadRequestException {
        SmsSuccess smsSuccess = new SmsSuccess();
        SmsRequestDetails smsRequestDetails = SmsRequestDetails.builder().
                message(smsRequest.getMessage()).
                phoneNumber(smsRequest.getPhoneNumber()).
                createdAt(new Date()).
                updatedAt(new Date()).
                build();
        try {
            smsRequestRepository.save(smsRequestDetails);
            kafkaTemplate.send("notification.send_sms", smsRequestDetails.getId().toString());
            smsSuccess.setComments("message Queued");
        }catch(Exception e){
            log.error("Could not save the  message request. Reason :{}", ExceptionUtils.getStackTrace(e));
            smsSuccess.setComments("message could Not be queued");
        }
        smsRequestDetails= smsRequestRepository.findSmsRequestDetailsById(smsRequestDetails.getId());
        smsSuccess.setRequestId(smsRequestDetails.getId());
        return smsSuccess;
    }
    @Override
    public BlacklistResponse addNumbersToBlacklist(BlacklistRequest blacklistRequest) {
        BlacklistResponse blacklistResponse = new BlacklistResponse() ;
        List<Blacklist> blacklist= blacklistRequest.getPhoneNumbersToBeBlacklisted().stream()
                .map(p-> Blacklist.builder().phoneNumber(p).build()).collect(Collectors.toList());
        try {
            blacklistRepository.saveAll(blacklist);
        } catch (Exception e){
            log.error("Could not save. Reason :{}", ExceptionUtils.getStackTrace(e));
        }
        for (String number : Optional.of(blacklistRequest).map(BlacklistRequest::getPhoneNumbersToBeBlacklisted).orElse(Collections.emptyList())) {
            if(Objects.equals(redisRepository.check(number),null)) {
                redisRepository.addToBlacklist(number);
            }
        }
        blacklistResponse.setData("Successfully Blacklisted");
        return blacklistResponse;
    }
    @Override
    public BlacklistResponse deleteNumbersFromBlacklist(BlacklistRequest blacklistRequest) {
        BlacklistResponse blacklistResponse = new BlacklistResponse();
        for (String number : Optional.ofNullable(blacklistRequest).map(BlacklistRequest::getPhoneNumbersToBeBlacklisted).orElse(Collections.emptyList()))
            redisRepository.removeFromBlacklist(number);

        if( blacklistRequest != null) {
            try {
                blacklistRepository.deleteByPhoneNumberIn(blacklistRequest.getPhoneNumbersToBeBlacklisted());
            } catch(Exception e){
                log.error("error deleting the phone numbers. Reason : {}", ExceptionUtils.getStackTrace(e));
            }
            blacklistResponse.setData("Successfully Whitelisted");
        }
        return blacklistResponse;
    }
    @Override
    public BlacklistNumbersResponse getBlacklistedNumbers() {
        BlacklistNumbersResponse blacklistNumbersResponse = new BlacklistNumbersResponse();
        List<Blacklist> blacklist = new ArrayList<>();
        try {
            blacklist = blacklistRepository.findAll();
        } catch(Exception e){
            log.error("Error finding the blacklisted numbers. Reason: {}", ExceptionUtils.getStackTrace(e));
        }
        List<String> blacklistedNumbers = blacklist.stream().map(Blacklist::getPhoneNumber).collect(Collectors.toList());
        blacklistNumbersResponse.setData(blacklistedNumbers);
        return blacklistNumbersResponse;
    }
    @Override
    public SmsRequestDetails getSmsDetails(Integer requestId) {
        SmsRequestDetails smsRequestDetails = new SmsRequestDetails();
        log.info("getting sms details");
        try {
            smsRequestDetails = smsRequestRepository.findSmsRequestDetailsById(requestId);
        } catch (Exception e) {
            log.error("error finding the sms : {}", ExceptionUtils.getStackTrace(e));
        }
        return smsRequestDetails;
    }

}
