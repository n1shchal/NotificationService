package com.learn.NotificationService.model.Imi;

import lombok.Data;

import java.util.List;

@Data
public class RequestDestination {
    private List<String> msisdn;
    private String correlationId;
}
