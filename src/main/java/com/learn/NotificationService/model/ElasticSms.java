package com.learn.NotificationService.model;


import com.learn.NotificationService.model.entity.SmsRequestDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.*;
import java.util.Date;

@Data
@Document(indexName = "sms_details")
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSms {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Integer)
    private Integer smsRequestId;
    @Field(type = FieldType.Text)
    private String phoneNumber ;
    @Field(type = FieldType.Text)
    private String message;
    @Field(type = FieldType.Integer)
    private Integer failureCode;
    @Field(type = FieldType.Integer)
    private Integer status;
    @Field(type = FieldType.Text)
    private String failureComments;
    @Field(type = FieldType.Date)
    private Date createdAt;
    @Field(type = FieldType.Date)
    private Date updatedAt;

    public ElasticSms(SmsRequestDetails message) {
        this.smsRequestId = message.getId();
        this.phoneNumber = message.getPhoneNumber();
        this.message = message.getMessage();
        this.status = message.getStatus();
        this.failureCode = message.getFailureCode();
        this.failureComments = message.getFailureComments();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }

}
