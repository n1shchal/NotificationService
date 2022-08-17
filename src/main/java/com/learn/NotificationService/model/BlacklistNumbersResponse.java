package com.learn.NotificationService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlacklistNumbersResponse {

    List<String> data;
}
