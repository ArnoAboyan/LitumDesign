package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findTop5ByOrderByCountOfDownloadsDesc();
    List<ProductEntity> findTop5ByOrderByCreatedAtDesc();

    List<ProductEntity> findAllByAdvertisingTrue();

    ProductEntity findByGdFileId(String fieldId);

}
