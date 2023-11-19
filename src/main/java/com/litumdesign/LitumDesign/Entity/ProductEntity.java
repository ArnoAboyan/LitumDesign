package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @ManyToOne()
    @JoinColumn(name = "upload_user_id")
    @ToString.Exclude
    UserEntity uploadUserId;
    @Column(name = "title", columnDefinition = "VARCHAR")
    String title;
    @Column(name = "price")
    Double price;
    @Column(name = "discount", columnDefinition = "INTEGER DEFAULT 0")
    Integer discount;
    @Column(name = "short_info", columnDefinition = "VARCHAR")
    String shortInfo;
    @Column(name = "description", columnDefinition = "VARCHAR")
    String description;
    @Column(name = "gd_file_id", columnDefinition = "VARCHAR")
    String gdFileId;
    @Column(name = "count_of_downloads", columnDefinition = "INTEGER DEFAULT 0")
    Integer countOfDownloads;
    @Column(name = "count_of_reviews", columnDefinition = "INTEGER DEFAULT 0" )
    Integer countOfReviews;
    @Column(name = "average_rating", columnDefinition = "INTEGER DEFAULT 0")
    Integer averageRating;
    @Column(name = "count_of_view", columnDefinition = "INTEGER DEFAULT 0")
    Integer countOfView;
    @Column(name = "title_image_link", columnDefinition = "VARCHAR")
    String titleImageLink;
    @Column(name = "license", columnDefinition = "VARCHAR")
    String license;
    @Enumerated(value = EnumType.STRING)
    Access access;

    @Enumerated(value = EnumType.STRING)
    Categories categories;
    @Enumerated(value = EnumType.STRING)
    GameType gameType;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductPhotoEntity> photoLink;
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<CommentProductEntity> comment;
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductVersionEntity> productVersion;

    @Column(name = "video_link", columnDefinition = "VARCHAR")
    String videoLink;

    @Column(name = "advertising", columnDefinition = "VARCHAR DEFAULT ")
    Boolean advertising;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }



    public ProductEntity(String title, Double price, String shortInfo, String license,
                         String description, Categories categories, GameType gameType,
                         Access access, String videoLink, Boolean advertising, Integer discount,
                         Integer countOfDownloads, Integer countOfReviews, Integer averageRating, Integer countOfView) {
        this.title = title;
        this.price = price;
        this.shortInfo = shortInfo;
        this.license = license;
        this.description = description;
        this.categories = categories;
        this.gameType = gameType;
        this.access = access;
        this.videoLink = videoLink;
        this.advertising = advertising;
        this.discount = discount;
        this.countOfDownloads = countOfDownloads;
        this.countOfReviews = countOfReviews;
        this.averageRating = averageRating;
        this.countOfView = countOfView;

    }
}
