package com.learn.NotificationService.service;


import com.learn.NotificationService.model.BlacklistRequest;
import com.learn.NotificationService.model.BlacklistResponse;
import com.learn.NotificationService.model.SmsRequest;
import com.learn.NotificationService.model.SmsResponse;
import com.learn.NotificationService.model.entity.Blacklist;
import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.repository.BlacklistRepository;
import com.learn.NotificationService.repository.SmsRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class NotificationService {

    private final SmsRequestRepository smsRequestRepository;

    private final BlacklistRepository blacklistRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;


    public NotificationService(SmsRequestRepository smsRequestRepository,
                               BlacklistRepository blacklistRepository,
                               KafkaTemplate<String, String> kafkaTemplate) {
        this.smsRequestRepository = smsRequestRepository;
        this.blacklistRepository = blacklistRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public SmsResponse sendAndSaveSms(SmsRequest smsRequest){
        SmsResponse smsResponse = new SmsResponse();
        SmsRequestDetails smsRequestDetails = smsRequestRepository.save(com.learn.NotificationService.model.entity.SmsRequestDetails.builder().
                message(smsRequest.getMessage()).failureCode(1).failureComments("").phoneNumber(smsRequest.getPhoneNumber()).status(1).build());
        smsResponse.setComments(smsRequestDetails.getFailureComments());
        smsResponse.setRequestId(smsRequestDetails.getId());
        kafkaTemplate.send("notification.send_sms", "lmao");
        return smsResponse;
    }

    public String addNumbersToBlacklist(BlacklistRequest blacklistRequest){
        for(String number:blacklistRequest.getPhoneNumbersToBeBlacklisted())
            blacklistRepository.save(Blacklist.builder().phoneNumber(number).build());
        return "Successfully Blacklisted";
    }

    public String deleteNumbersFromBlacklist(BlacklistRequest blacklistRequest){
        blacklistRepository.deleteByPhoneNumberIn(blacklistRequest.getPhoneNumbersToBeBlacklisted());
        return "Deleted Successfully";
    }

    public BlacklistResponse getBlacklistedNumbers(){
        BlacklistResponse blacklistResponse = new BlacklistResponse();
        List<String> blacklistedNumbers = new ArrayList<>();
        List<Blacklist> blacklist = blacklistRepository.findAll();
        for(Blacklist blacklistedNumber:blacklist){
            blacklistedNumbers.add(blacklistedNumber.getPhoneNumber());
        }
        blacklistResponse.setData(blacklistedNumbers);
        return blacklistResponse;
    }

    public SmsRequestDetails getSmsDetails(Integer requestId){
        Optional<SmsRequestDetails> smsRequestDetails = Optional.of(new SmsRequestDetails());
        log.info("getting sms details");
        try {
            smsRequestDetails = smsRequestRepository.findById(requestId);
        }catch(Exception e){
            log.error("error finding the sms : {}", ExceptionUtils.getStackTrace(e));
        }
        log.info("sms getails : {}", smsRequestDetails.get());
        return smsRequestDetails.get();
    }

}
