package com.app.publishsubscribe.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "publishers")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String schedule;
    private Date createdDate;
    private Date updatedDate;
}