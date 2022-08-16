package com.learn.NotificationService.service;


import com.learn.NotificationService.model.BlacklistRequest;
import com.learn.NotificationService.model.BlacklistResponse;
import com.learn.NotificationService.model.SmsRequest;
import com.learn.NotificationService.model.SmsResponse;
import com.learn.NotificationService.model.entity.Blacklist;
import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.repository.BlacklistRepository;
import com.learn.NotificationService.repository.RedisRepository;
import com.learn.NotificationService.repository.SmsRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;

import static java.util.Objects.isNull;

@Slf4j
@Component
public class NotificationService {

    private final SmsRequestRepository smsRequestRepository;
    private final BlacklistRepository blacklistRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisRepository redisRepository;


    public NotificationService(SmsRequestRepository smsRequestRepository,
                               BlacklistRepository blacklistRepository,
                               KafkaTemplate<String, String> kafkaTemplate, RedisRepository redisRepository) {
        this.smsRequestRepository = smsRequestRepository;
        this.blacklistRepository = blacklistRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.redisRepository = redisRepository;
    }

    public SmsResponse sendAndSaveSms(SmsRequest smsRequest) throws BadRequestException {
        SmsResponse smsResponse = new SmsResponse();
        //put save and kafka in try catch
        SmsRequestDetails smsRequestDetails = smsRequestRepository.save(SmsRequestDetails.builder().
                message(smsRequest.getMessage()).phoneNumber(smsRequest.getPhoneNumber()).createdAt(new Date()).updatedAt(new Date()).build());

        kafkaTemplate.send("notification.send_sms", smsRequestDetails.getId().toString());
        smsResponse.setComments(smsRequestDetails.getFailureComments());
        smsResponse.setRequestId(smsRequestDetails.getId());
        return smsResponse;
    }

    public String addNumbersToBlacklist(BlacklistRequest blacklistRequest) {

        // add check if request is not null and list is not empty
        //use batching to save (use saveAll)

        for (String number : Optional.ofNullable(blacklistRequest).map(BlacklistRequest::getPhoneNumbersToBeBlacklisted).orElse(Collections.emptyList())) {
            blacklistRepository.save(Blacklist.builder().phoneNumber(number).build());
            log.info("the number {} is {} ", number, redisRepository.check(number));
            redisRepository.addToBlacklist(number);
        }

        //add these numbers to redis too


        return "Successfully Blacklisted";
    }

    public String deleteNumbersFromBlacklist(BlacklistRequest blacklistRequest) {
        for (String number : Optional.ofNullable(blacklistRequest).map(BlacklistRequest::getPhoneNumbersToBeBlacklisted).orElse(Collections.emptyList()))
            redisRepository.removeFromBlacklist(number);

        blacklistRepository.deleteByPhoneNumberIn(blacklistRequest.getPhoneNumbersToBeBlacklisted());
        return "Deleted Successfully";
    }

    public BlacklistResponse getBlacklistedNumbers() {
        BlacklistResponse blacklistResponse = new BlacklistResponse();
        List<String> blacklistedNumbers = new ArrayList<>();
        List<Blacklist> blacklist = blacklistRepository.findAll();

        // add null check and empty check on list
        for (Blacklist blacklistedNumber : blacklist) {
            blacklistedNumbers.add(blacklistedNumber.getPhoneNumber());
        }
        blacklistResponse.setData(blacklistedNumbers);
        return blacklistResponse;
    }

    public SmsRequestDetails getSmsDetails(Integer requestId) {
        Optional<SmsRequestDetails> smsRequestDetails = Optional.of(new SmsRequestDetails());
        log.info("getting sms details");
        try {
            smsRequestDetails = smsRequestRepository.findById(requestId);
        } catch (Exception e) {
            log.error("error finding the sms : {}", ExceptionUtils.getStackTrace(e));
        }
        //return 404 in case of not found
        log.info("sms getails : {}", smsRequestDetails.get());
        return smsRequestDetails.get();
    }

}
