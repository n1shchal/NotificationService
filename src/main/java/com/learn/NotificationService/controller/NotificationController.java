package com.learn.NotificationService.controller;

import com.learn.NotificationService.model.BlacklistRequest;
import com.learn.NotificationService.model.BlacklistResponse;
import com.learn.NotificationService.model.SmsRequest;
import com.learn.NotificationService.model.SmsResponse;
import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;


@Slf4j
@RestController
@Component
@RequestMapping("/v1")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Consumes("application/json")
    @Produces("application/json")
    @PostMapping("/sms/send")
    public ResponseEntity<SmsResponse> sendSms(@RequestBody SmsRequest smsRequest) {
        SmsResponse smsResponse = new SmsResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            log.info("sms request recieved : {}", smsRequest);
            smsResponse = notificationService.sendAndSaveSms(smsRequest);
        } catch (BadRequestException e) {
            log.error("BadRequestException {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.info("got response: {}", smsResponse.toString());
        return ResponseEntity.status(httpStatus).body(smsResponse);

    }

    @Consumes("application/json")
    @Produces("application/json")
    @PostMapping("/blacklist")
    public ResponseEntity<String> addNumbersToBlacklist(@RequestBody BlacklistRequest blacklistRequest) {
        String response = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            log.info("request recieved to blacklist numbers : {}", blacklistRequest);
            response = notificationService.addNumbersToBlacklist(blacklistRequest);
        } catch (BadRequestException e) {
            log.error("BadRequestException {}", e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(response);
    }
    @Produces("application/json")
    @GetMapping("/blacklist")
    public ResponseEntity<BlacklistResponse> getBlacklistedNumbers() {
        BlacklistResponse blacklistResponse = new BlacklistResponse()
;        HttpStatus httpStatus = HttpStatus.OK;
        try {
            log.info("getting blacklisted numbers ");
            blacklistResponse = notificationService.getBlacklistedNumbers();
        } catch (BadRequestException e) {
            log.error("BadRequestException {}", e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(blacklistResponse);
    }
    @Consumes("application/json")
    @Produces("application/json")
    @DeleteMapping("/blacklist")
    public ResponseEntity<String> deleteNumbersFromBlacklist(@RequestBody BlacklistRequest blacklistRequest) {
        String response = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            log.info("request recieved to delete numbers from the blacklist: {}", blacklistRequest);
            response = notificationService.deleteNumbersFromBlacklist(blacklistRequest);
        } catch (BadRequestException e) {
            log.error("BadRequestException {}", e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(response);
    }

    //custom exception handling
    @GetMapping("/sms")
    public ResponseEntity<SmsRequestDetails> getSmsDetails(@RequestParam(value = "request_id") Integer orderId) {
        SmsRequestDetails smsRequestDetails = new SmsRequestDetails();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            log.info("request recieved for order details for Order ID : {}", orderId);
            smsRequestDetails = notificationService.getSmsDetails(orderId);
        } catch (BadRequestException e) {
            log.error("BadRequestException {}", e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(smsRequestDetails);
    }
}
