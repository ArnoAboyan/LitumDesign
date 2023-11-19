package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "product_photo")
@Data
@NoArgsConstructor
public class ProductPhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "product_entity")
    @ToString.Exclude
    ProductEntity productEntity;

    @Column(name = "photo_link", columnDefinition = "VARCHAR")
    String photoLink;


    public ProductPhotoEntity(ProductEntity productEntity, String photoLink) {
        this.productEntity = productEntity;
        this.photoLink = photoLink;
    }

}
