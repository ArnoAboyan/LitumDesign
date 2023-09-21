package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.NewsEntity;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
@Repository
public interface NewsEntityRepository extends JpaRepository<NewsEntity, Long> {


    Page<NewsEntity> findAllByOrderByCreatedAt (Pageable pageable);


}


