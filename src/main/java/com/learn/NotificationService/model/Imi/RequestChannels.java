package com.learn.NotificationService.model.Imi;

import lombok.Data;
import org.hibernate.mapping.Collection;

import java.util.Collections;
import java.util.List;

@Data
public class RequestChannels {

//    private List<SmartLink> smartLinks;
    private RequestBody sms;
    public RequestChannels(){
        this.sms = new RequestBody();
//        this.smartLinks = Collections.singletonList(new SmartLink());
    }
}
