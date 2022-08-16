package com.learn.NotificationService.model.Imi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImiRequest {

    @JsonProperty("deliverychannel")
    private String deliverychannel = "sms";

    private RequestChannels channels;

    private List<RequestDestination> destination;

    public ImiRequest(){
        this.channels = new RequestChannels();
        this.destination = Collections.singletonList(new RequestDestination());
    }

}
