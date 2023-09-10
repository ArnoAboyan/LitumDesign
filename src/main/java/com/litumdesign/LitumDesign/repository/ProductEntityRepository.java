package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductEntityRepository extends CrudRepository<ProductEntity, Long> {

    List<ProductEntity> findTop5ByOrderByCountOfDownloadsDesc();
    List<ProductEntity> findTop5ByOrderByCreatedAtDesc();

}
