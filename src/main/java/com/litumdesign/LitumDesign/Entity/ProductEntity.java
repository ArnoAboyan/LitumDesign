package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    String price;
    String discount;
    String shopInfo;
    @Column(name = "short_info")
    String shortInfo;
    String description;
    @Column(name = "gd_link")
    String gdLink;
    @Column(name = "count_of_downloads")
    String countOfDownloads;
    @Column(name = "count_of_reviews")
    String countOfReviews;
    @Column(name = "average_rating")
    String averageRating;
    Categories categories;
    GameType gameType;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ProductPhoto> photoLink;

    @Column(name = "video_link")
    String videoLink;


    public ProductEntity(UserEntity uploadUserId, String title, String price, String discount, String shopInfo, String shortInfo, String description, String gdLink, String countOfDownloads, String countOfReviews, String averageRating, Categories categories, GameType gameType, String videoLink) {
        this.uploadUserId = uploadUserId;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.shopInfo = shopInfo;
        this.shortInfo = shortInfo;
        this.description = description;
        this.gdLink = gdLink;
        this.countOfDownloads = countOfDownloads;
        this.countOfReviews = countOfReviews;
        this.averageRating = averageRating;
        this.categories = categories;
        this.gameType = gameType;
        this.videoLink = videoLink;
    }
}
