package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.CommentProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentProductRepository extends JpaRepository<CommentProductEntity, Long> {

    List<CommentProductEntity> findAllByProductEntityIdAndParentIsNull(Long productId);

}
