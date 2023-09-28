package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@RequiredArgsConstructor
@Data
@Table(name = "comment_product")
@Transactional
public class CommentProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "product_entity")
    @ToString.Exclude
    ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "user_entity")
    @ToString.Exclude
    UserEntity userEntity;

    @Column(name = "comment", length = 0)
    String comment;

    public CommentProductEntity(ProductEntity productEntity, UserEntity userEntity, String photoLink) {
        this.productEntity = productEntity;
        this.userEntity = userEntity;
        this.comment = photoLink;
    }


}
