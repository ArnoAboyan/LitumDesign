package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;

    @ManyToOne
    @JoinColumn(name = "upload_user_id")
    @ToString.Exclude
    UserEntity uploadUserId;
    String title;
    Double price;
    Integer discount;
    @Column(name = "short_info")
    String shortInfo;
    String description;
    @Column(name = "gd_file_id")
    String gdFileId;
    @Column(name = "count_of_downloads")
    Integer countOfDownloads;
    @Column(name = "count_of_reviews")
    Integer countOfReviews;
    @Column(name = "average_rating")
    Integer averageRating;
    @Column(name = "title_image_link")
    String titleImageLink;
    @Enumerated(value = EnumType.STRING)
    Access access;
    @Enumerated(value = EnumType.STRING)
    Categories categories;
    @Enumerated(value = EnumType.STRING)
    GameType gameType;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ProductPhotoEntity> photoLink;
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<CommentProductEntity> comment;

    @Column(name = "video_link")
    String videoLink;

    @Column(name = "advertising")
    Boolean advertising;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }



    public ProductEntity(String title, String titleImageLink, Double price, String shortInfo, String description, Categories categories, GameType gameType, Access access, String videoLink) {
        this.title = title;
        this.titleImageLink = titleImageLink;
        this.price = price;
        this.shortInfo = shortInfo;
        this.description = description;
        this.categories = categories;
        this.gameType = gameType;
        this.access = access;
        this.videoLink = videoLink;
    }
}
