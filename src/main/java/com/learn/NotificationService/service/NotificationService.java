package com.learn.NotificationService.service;

import com.learn.NotificationService.model.BlacklistRequest;
import com.learn.NotificationService.model.BlacklistResponse;
import com.learn.NotificationService.model.SmsRequest;
import com.learn.NotificationService.model.SmsResponse;
import com.learn.NotificationService.model.entity.SmsRequestDetails;

public interface NotificationService {
    SmsResponse sendAndSaveSms(SmsRequest smsRequest);

    String addNumbersToBlacklist(BlacklistRequest blacklistRequest);

    String deleteNumbersFromBlacklist(BlacklistRequest blacklistRequest);

    BlacklistResponse getBlacklistedNumbers();

    SmsRequestDetails getSmsDetails(Integer requestId);

    }
