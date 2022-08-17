package com.learn.NotificationService.utils;


public enum Enums {

    MESSAGE_QUEUED(0, "Message is Queued"),

    PHONE_NUMBER_BLACKLISTED(1, "Phone Number is Blacklisted"),
    SEND_SUCCESS(2,"Sms Sent Successfully"),
    SEND_ERROR(3, "Error via sending message to third party");

    public final String text;
    public final Integer value;

    Enums(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

}
