package com.learn.NotificationService.controller;

import com.learn.NotificationService.model.ElasticSms;
import com.learn.NotificationService.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@Component
@RequestMapping("/v1/ES")
public class ElasticSearchController {

    private final ElasticSearchService elasticSearchService;

    public ElasticSearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/getByText")
    public List<ElasticSms> getByText(@RequestParam String text ){
        return  elasticSearchService.getByText(text);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @GetMapping("/getByPhone")
    public List<ElasticSms> getByPhone(@RequestParam String phoneNumber,
                                       @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                       @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        return elasticSearchService.getByPhone(phoneNumber, from, to);
    }

}
