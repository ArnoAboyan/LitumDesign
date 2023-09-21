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

    @Column(name = "photo", columnDefinition = "VARCHAR")
    private String photoLink;

    @Column(name = "title")
    private String title;

    @Column(name = "short_text")
    private String shortText;

    @Column(name = "text", columnDefinition = "VARCHAR")
    private String text;

    @Column(name = "about")
    private String about;

    @Column(name = "author")
    private String author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }


    public NewsEntity(String photoLink,String shortText, String title, String text, String about, String author) {
        this.photoLink = photoLink;
        this.shortText = shortText;
        this.title = title;
        this.text = text;
        this.about = about;
        this.author = author;
    }
}
