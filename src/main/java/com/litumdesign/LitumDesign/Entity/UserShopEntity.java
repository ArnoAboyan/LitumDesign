package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "user_shop")
@Data
@NoArgsConstructor

public class UserShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    UserEntity user;

    @Column(name = "shop-url")
    String shopUrl;
    @Column(name = "shop_name")
    String shopName;

    public UserShopEntity(UserEntity user, String shopUrl, String shopName) {
        this.user = user;
        this.shopUrl = shopUrl;
        this.shopName = shopName;
    }
}
