package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "comment_product")
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


    @Column(name = "created_at")
    LocalDateTime createdAt;
    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommentProductEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentProductEntity> children = new ArrayList<>();


    public CommentProductEntity(ProductEntity productEntity, UserEntity userEntity, String comment) {
        this.productEntity = productEntity;
        this.userEntity = userEntity;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentProductEntity{" +
                "id=" + id +
                ", productEntity=" + productEntity +
                ", userEntity=" + userEntity +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", parent=" + (parent != null ? parent.getId() : null) +
                ", children=" + children +
                '}';
    }

}
