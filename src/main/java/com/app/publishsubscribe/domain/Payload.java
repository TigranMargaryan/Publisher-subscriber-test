package com.app.publishsubscribe.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payloads")
public class Payload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "data", nullable = false)
    private String data;

    @CreationTimestamp
    private LocalDate created;

    @UpdateTimestamp
    private LocalDate updated;

}