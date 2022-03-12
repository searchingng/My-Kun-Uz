package com.company.entity;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "comment")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

    private LocalDateTime createdDate;
}
