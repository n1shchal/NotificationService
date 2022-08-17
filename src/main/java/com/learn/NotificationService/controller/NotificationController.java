package com.learn.NotificationService.controller;

import com.learn.NotificationService.model.*;
import com.learn.NotificationService.model.entity.SmsRequestDetails;
import com.learn.NotificationService.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
            if(smsRequest.getPhoneNumber() == null || smsRequest.getMessage()== null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Phone number or message missing");
            log.info("sms request recieved : {}", smsRequest);
            smsResponse.setData(notificationService.sendAndSaveSms(smsRequest));
        } catch (BadRequestException |HttpClientErrorException e) {
            SmsError smsError = new SmsError(String.valueOf(HttpStatus.BAD_REQUEST), e.getMessage());
            smsResponse.setError(smsError);
            log.error("BadRequestException {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            SmsError smsError = new SmsError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage());
            smsResponse.setError(smsError);
            log.error("Exception {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(smsResponse);

    }

    @Consumes("application/json")
    @Produces("application/json")
    @PostMapping("/blacklist")
    public ResponseEntity<BlacklistResponse> addNumbersToBlacklist(@RequestBody BlacklistRequest blacklistRequest) {
        BlacklistResponse response = new BlacklistResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if(blacklistRequest==null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Null Request");
            log.info("request recieved to blacklist numbers : {}", blacklistRequest);
            response = notificationService.addNumbersToBlacklist(blacklistRequest);
        } catch (BadRequestException | HttpClientErrorException e) {
            log.error("BadRequestException {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(response);
    }
    @Produces("application/json")
    @GetMapping("/blacklist")
    public ResponseEntity<BlacklistNumbersResponse> getBlacklistedNumbers() {
        BlacklistNumbersResponse blacklistNumbersResponse = new BlacklistNumbersResponse()
;        HttpStatus httpStatus = HttpStatus.OK;
        try {
            log.info("getting blacklisted numbers ");
            blacklistNumbersResponse = notificationService.getBlacklistedNumbers();
        } catch (BadRequestException e) {
            log.error("BadRequestException {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(blacklistNumbersResponse);
    }
    @Consumes("application/json")
    @Produces("application/json")
    @DeleteMapping("/blacklist")
    public ResponseEntity<BlacklistResponse> deleteNumbersFromBlacklist(@RequestBody BlacklistRequest blacklistRequest) {
        BlacklistResponse blacklistResponse = new BlacklistResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if(blacklistRequest==null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Null Request");
            log.info("request recieved to delete numbers from the blacklist: {}", blacklistRequest);
            blacklistResponse = notificationService.deleteNumbersFromBlacklist(blacklistRequest);
        } catch (BadRequestException | HttpClientErrorException e) {
            log.error("BadRequestException {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("Exception {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(blacklistResponse);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @GetMapping("/sms")
    public ResponseEntity<SmsDetailResponse> getSmsDetails(@RequestParam(value = "request_id") Integer orderId) {
        SmsRequestDetails smsRequestDetails;
        SmsDetailResponse smsDetailResponse = new SmsDetailResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if(orderId==null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "OrderId is null");
            log.info("request recieved for order details for Order ID : {}", orderId);
            smsRequestDetails = notificationService.getSmsDetails(orderId);
            if(smsRequestDetails==null)
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No order found for given id");
            smsDetailResponse.setData(smsRequestDetails);
        } catch (BadRequestException e) {
            log.error("BadRequestException {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.BAD_REQUEST;
            SmsError smsError = new SmsError(String.valueOf(HttpStatus.BAD_REQUEST), e.getMessage());
            smsDetailResponse.setError(smsError);
        } catch(HttpClientErrorException e){
            log.error("BadRequestException {}", ExceptionUtils.getStackTrace(e));
            httpStatus = HttpStatus.NOT_FOUND;
            SmsError smsError = new SmsError(String.valueOf(HttpStatus.NOT_FOUND), e.getMessage());
            smsDetailResponse.setError(smsError);
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SmsError smsError = new SmsError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage());
            smsDetailResponse.setError(smsError);
        }
        return ResponseEntity.status(httpStatus).body(smsDetailResponse);
    }
}
