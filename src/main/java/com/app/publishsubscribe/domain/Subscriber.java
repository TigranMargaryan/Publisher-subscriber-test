package com.app.publishsubscribe.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "subscriber")
    private List<Message> messages;
    private Date createdDate;
    private Date updatedDate;

}
