package com.litumdesign.LitumDesign.Entity;
//


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor()
public class UserEntity {

    @Id
    String username;

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

    String userShop;
    @Column(name = "discord_tag")
    String discordTag;
    @Column(name = "telegram_tag")
    String telegramTag;
    @Column(name = "count_of_downloads")
    int countOfDownloads;
    @Column(name = "count_of_uploads")
    int countOfUploads;

    String locale;

    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public UserEntity(String username, String sub, String password, String email, String name, String imageUrl, LoginProvider provider, LocalDateTime createdAt, Rank rank, String fullName, Role authorities, String discordTag, String telegramTag, int countOfDownloads, int countOfUploads, String locale) {
        this.username = username;
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
    }


    //    public UserEntity(String username, String password, String email, String name, String imageUrl, LoginProvider provider, Role authorities) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.name = name;
//        this.imageUrl = imageUrl;
//        this.provider = provider;
//        this.authorities = authorities;
//    }

//    public void addAuthority(AuthorityEntity authority) {
//
//        if (userAuthorities.stream().anyMatch(uae -> uae.getUser().equals(this) && uae.getAuthority().equals(authority))) {
//            return;
//        }
//
//        UserAuthorityEntity userAuthority = new UserAuthorityEntity(this, authority);
//
//        this.userAuthorities.add(userAuthority);
//        authority.getAssignedTo().add(userAuthority);
//    }

//    public void removeAuthority(AuthorityEntity authority) {
//
//        Iterator<UserAuthorityEntity> iterator = userAuthorities.iterator();
//
//        while (iterator.hasNext()) {
//            UserAuthorityEntity next = iterator.next();
//
//            if (next.getAuthority().equals(authority)) {
//                iterator.remove();
//                authority.getAssignedTo().remove(next);
//                next.setUser(null);
//                next.setAuthority(null);
//            }
//
//        }
//
//    }

//    public void mergeAuthorities(List<AuthorityEntity> authorities) {
//        var toRemove = this
//                .userAuthorities
//                .stream()
//                .filter(uae -> !authorities.contains(uae.getAuthority()))
//                .toList();
//
//        toRemove.forEach(uae -> this.removeAuthority(uae.getAuthority()));
//
//        authorities.forEach(this::addAuthority);
//    }
}
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.*;
//import org.hibernate.validator.constraints.UniqueElements;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
//@Entity
//@Data
//@Table(name = "users")
//@RequiredArgsConstructor
//@Builder
//@AllArgsConstructor
//public class UserEntity  {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Long userId;
//
//    @Column(name = "nick_name")
//    private String userNickName;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "password")
//    private String password;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<UserAuthorityEntity> userAuthorities = new ArrayList<>();
//
//    @Column(name = "sub")
//    private String sub;
//
//    @Enumerated(EnumType.STRING)
//    private Rank rank;
//
//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
//
//    @Column(name = "shop_name")
//    private String shopName;
//
//    @Column(name = "shop_url")
//    private String shopUrl;
//
//    @Column(name = "image_url")
//    private String imageUrl;
//
//    @Column(name = "discord_tag")
//    private String discordTag;
//
//    @Column(name = "telegram_tag")
//    private String telegramTag;
//
//    @Column(name = "count_of_downloads")
//    private int countOfDownloads;
//
//    @Column(name = "count_of_uploads")
//    private int countOfUploads;
//
//    @Enumerated(EnumType.STRING)
//    private LoginProvider provider;
//    @Column(name = "create_at")
//    private LocalDateTime createAt;
//
// @PrePersist
//        void  assignCreateAt(){
//            this.createAt=LocalDateTime.now();
//    }
//
//    public void addAuthority(AuthorityEntity authority) {
//
//        if (userAuthorities.stream().anyMatch(uae -> uae.getUser().equals(this) && uae.getAuthority().equals(authority))) {
//            return;
//        }
//
//        UserAuthorityEntity userAuthority = new UserAuthorityEntity(this, authority);
//
//        this.userAuthorities.add(userAuthority);
//        authority.getAssignedTo().add(userAuthority);
//    }
//
//    public void removeAuthority(AuthorityEntity authority) {
//
//        Iterator<UserAuthorityEntity> iterator = userAuthorities.iterator();
//
//        while (iterator.hasNext()) {
//            UserAuthorityEntity next = iterator.next();
//
//            if (next.getAuthority().equals(authority)) {
//                iterator.remove();
//                authority.getAssignedTo().remove(next);
//                next.setUser(null);
//                next.setAuthority(null);
//            }
//
//        }
//
//    }
//
//    public void mergeAuthorities(List<AuthorityEntity> authorities) {
//        var toRemove = this
//                .userAuthorities
//                .stream()
//                .filter(uae -> !authorities.contains(uae.getAuthority()))
//                .toList();
//
//        toRemove.forEach(uae -> this.removeAuthority(uae.getAuthority()));
//
//        authorities.forEach(this::addAuthority);
//    }
//}
//
