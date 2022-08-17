package com.learn.NotificationService.repository;

import com.learn.NotificationService.model.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
    @Transactional
    void deleteByPhoneNumberIn(List<String> phoneNumber);

    Blacklist findBlacklistById(Integer id);
}
