package com.learn.NotificationService.repository;

import com.learn.NotificationService.model.ElasticSms;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;

public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticSms, String> {

    List<ElasticSms> findByMessage(String message);
    List<ElasticSms> findByPhoneNumberAndCreatedAtAfterAndCreatedAtBefore(String phoneNumber, Date dateFrom, Date dateTill);
}
