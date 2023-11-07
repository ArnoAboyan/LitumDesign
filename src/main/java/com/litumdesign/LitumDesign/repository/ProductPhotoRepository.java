package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.ProductPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProductPhotoRepository extends JpaRepository<ProductPhotoEntity, Long> {
    void deleteByProductEntity(@Param("ProductEntity") ProductEntity productEntity);
}
