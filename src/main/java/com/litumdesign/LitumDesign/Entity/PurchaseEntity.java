package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "purchase")
@Data
@NoArgsConstructor
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_login") // Имя столбца, который связывает с таблицей пользователей
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id") // Имя столбца, который связывает с таблицей продуктов
    private ProductEntity product;

    // Другие поля, конструкторы, геттеры, сеттеры и т.д.

    public PurchaseEntity(UserEntity user, ProductEntity product) {
        this.user = user;
        this.product = product;
    }
}
