package com.learn.NotificationService.model;

import com.learn.NotificationService.model.entity.SmsRequestDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsDetailResponse {

    SmsRequestDetails data;
    SmsError error;
}
