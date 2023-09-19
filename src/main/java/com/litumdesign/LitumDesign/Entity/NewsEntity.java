package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo")
    private String photoLink;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "about")
    private String about;

    @Column(name = "author")
    private String author;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }


    public NewsEntity(String photoLink, String title, String text, String about, String author) {
        this.photoLink = photoLink;
        this.title = title;
        this.text = text;
        this.about = about;
        this.author = author;
    }
}
