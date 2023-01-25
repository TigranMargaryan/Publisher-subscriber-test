package com.app.publishsubscribe.repository;

import com.app.publishsubscribe.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySubscriberAndCreatedGreaterThanEqual(Subscriber subscriber, LocalDate created, Pageable pageable);
    List<Message> findAllBySubscriber(Subscriber subscriber, Pageable pageable);
}