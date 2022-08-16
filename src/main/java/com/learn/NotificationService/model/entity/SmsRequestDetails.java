package com.learn.NotificationService.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "sms_requests")
public class SmsRequestDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    String phoneNumber;

    String message;

    Integer status;

    Integer failureCode;

    String failureComments;

    Date createdAt ;

    Date updatedAt;

}
