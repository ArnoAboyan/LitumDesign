package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_version")
@Data
@NoArgsConstructor
public class ProductVersionEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "product_entity")
    @ToString.Exclude
    ProductEntity productEntity;

    @Column(name = "version")
    String version;

    @Column(name = "version_comment")
    String versionComment;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    void assignCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }


    public ProductVersionEntity(ProductEntity productEntity, String version, String versionComment) {
        this.productEntity = productEntity;
        this.version = version;
        this.versionComment = versionComment;
    }
}
