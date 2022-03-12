package com.company.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "message")
@Setter
@Getter
@NoArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String subject;
    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;
    private Boolean used = false;
    private LocalDateTime date;
    private LocalDateTime usedDate;

}
