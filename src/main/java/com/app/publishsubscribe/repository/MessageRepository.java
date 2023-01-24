package com.app.publishsubscribe.repository;

import com.app.publishsubscribe.domain.Message;
import com.app.publishsubscribe.domain.Subscriber;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySubscriberAndCreatedGreaterThanEqual(Subscriber subscriber, Date created, Pageable pageable);
    List<Message> findAllBySubscriber(Subscriber subscriber, Pageable pageable);
}