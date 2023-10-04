package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.Access;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findTop5ByOrderByCountOfDownloadsDesc();
    List<ProductEntity> findTop5ByOrderByCreatedAtDesc();

    List<ProductEntity> findAllByAdvertisingTrueAndAccess(@Param("access") Access access);

    ProductEntity findByGdFileId(String fieldId);


}

