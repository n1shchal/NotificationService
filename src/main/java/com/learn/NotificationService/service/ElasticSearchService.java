package com.learn.NotificationService.service;

import com.learn.NotificationService.model.ElasticSms;
import com.learn.NotificationService.repository.ElasticSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ElasticSearchService {
        private final ElasticSearchRepository elasticsearchRepository;
        private RestHighLevelClient restHighLevelClient;


    public ElasticSearchService(ElasticSearchRepository elasticsearchRepository) {
            this.elasticsearchRepository = elasticsearchRepository;
        }

        public void save(ElasticSms elasticSms) {
            elasticsearchRepository.save(elasticSms);
        }

        public List<ElasticSms> getByText(String text) {
            return elasticsearchRepository.findByMessage(text);
        }

        public List<ElasticSms> getByPhone(String phoneNumber, Date from, Date to) {
            return  elasticsearchRepository.findByPhoneNumberAndCreatedAtAfterAndCreatedAtBefore(phoneNumber, from, to);
        }

}
