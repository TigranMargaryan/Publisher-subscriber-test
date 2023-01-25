package com.app.publishsubscribe.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "payloads")
public class Payload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;

    private String data;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date updated;
}