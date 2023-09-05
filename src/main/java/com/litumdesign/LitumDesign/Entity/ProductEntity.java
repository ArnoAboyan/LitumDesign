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
    int price;
    int discount;
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

    @Enumerated(value = EnumType.STRING)
    Categories categories;
    @Enumerated(value = EnumType.STRING)
    GameType gameType;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ProductPhotoEntity> photoLink;

    @Column(name = "video_link")
    String videoLink;


    public ProductEntity(String title, int price, String shopInfo, String shortInfo, String description, Categories categories, GameType gameType, String videoLink) {
        this.title = title;
        this.price = price;
        this.shopInfo = shopInfo;
        this.shortInfo = shortInfo;
        this.description = description;
        this.categories = categories;
        this.gameType = gameType;
        this.videoLink = videoLink;
    }
}
