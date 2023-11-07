package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.Access;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.ProductVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVersionRepository extends JpaRepository<ProductVersionEntity, Long> {

    List<ProductVersionEntity> findProductVersionEntitiesByProductEntityOrderByVersion(@Param("ProductEntity") ProductEntity productEntity);
}
