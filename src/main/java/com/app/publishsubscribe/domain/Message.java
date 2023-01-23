package com.app.publishsubscribe.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Object payload;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;
    private Date created;
    private Date updated;
}
