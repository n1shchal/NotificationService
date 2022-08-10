package com.learn.NotificationService.repository;

import com.learn.NotificationService.model.entity.SmsRequestDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SmsRequestRepository extends JpaRepository<SmsRequestDetails, Integer> {

    Optional<SmsRequestDetails> findById(Integer integer);
}
