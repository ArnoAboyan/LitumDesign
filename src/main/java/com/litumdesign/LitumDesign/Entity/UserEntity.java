package com.litumdesign.LitumDesign.Entity;
//


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor

public class UserEntity {

    @Id
    String login;

    @Column(unique = true)
    String sub;
    String password;
    @Column(unique = true)
    String email;
    String name; //    Nickname
    @Column(name = "image_url")
    String imageUrl;
    @Enumerated(value = EnumType.STRING)
    LoginProvider provider;
    @Column(name = "created_at")
    LocalDateTime createdAt;
    //    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    List<UserAuthorityEntity> userAuthorities = new ArrayList<>();
    @Enumerated(value = EnumType.STRING)
    Rank rank;

    @Column(name = "full_name")
    String fullName;
    @Enumerated(value = EnumType.STRING)
    Role authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<UserShopEntity> userShop = new ArrayList<>();

    @OneToMany(mappedBy = "uploadUserId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductEntity> product = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<CommentProductEntity> comment = new ArrayList<>();

    @Column(name = "discord_tag")
    String discordTag;
    @Column(name = "telegram_tag")
    String telegramTag;
    @Column(name = "count_of_downloads")
    Integer countOfDownloads;
    @Column(name = "count_of_uploads")
    Integer countOfUploads;

    Boolean verified;
    String locale;

    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public UserEntity(String login,
                      String sub,
                      String password,
                      String email,
                      String name,
                      String imageUrl,
                      LoginProvider provider,
                      LocalDateTime createdAt,
                      Rank rank,
                      String fullName,
                      Role authorities,
                      String discordTag,
                      String telegramTag,
                      Integer countOfDownloads,
                      Integer countOfUploads,
                      String locale,
                      Boolean verified) {
        this.login = login;
        this.sub = sub;
        this.password = password;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.createdAt = createdAt;
        this.rank = rank;
        this.fullName = fullName;
        this.authorities = authorities;
        this.discordTag = discordTag;
        this.telegramTag = telegramTag;
        this.countOfDownloads = countOfDownloads;
        this.countOfUploads = countOfUploads;
        this.locale = locale;
        this.verified = verified;
    }
}
