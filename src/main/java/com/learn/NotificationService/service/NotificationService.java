package com.learn.NotificationService.service;

import com.learn.NotificationService.model.*;
import com.learn.NotificationService.model.entity.SmsRequestDetails;

public interface NotificationService {
    SmsSuccess sendAndSaveSms(SmsRequest smsRequest);

    BlacklistResponse addNumbersToBlacklist(BlacklistRequest blacklistRequest);

    BlacklistResponse deleteNumbersFromBlacklist(BlacklistRequest blacklistRequest);

    BlacklistNumbersResponse getBlacklistedNumbers();

    SmsRequestDetails getSmsDetails(Integer requestId);

    }
