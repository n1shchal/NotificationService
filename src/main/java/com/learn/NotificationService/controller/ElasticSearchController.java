package com.learn.NotificationService.controller;

import com.learn.NotificationService.model.ElasticSms;
import com.learn.NotificationService.service.impl.ElasticSearchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Component
@RequestMapping("/v1/ES")
public class ElasticSearchController {

    private final ElasticSearchServiceImpl elasticSearchService;

    public ElasticSearchController(ElasticSearchServiceImpl elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/getByText")
    public List<ElasticSms> getByText(@RequestParam String text ){
        return  elasticSearchService.getByText(text);
    }

    @GetMapping("/getByPhone")
    public List<ElasticSms> getByPhone(@RequestParam String phoneNumber,
                                       @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                       @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        return elasticSearchService.getByPhone(phoneNumber, from, to);
    }

}
