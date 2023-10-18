package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.Access;
import com.litumdesign.LitumDesign.Entity.Categories;
import com.litumdesign.LitumDesign.Entity.GameType;
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
    List<ProductEntity> findTop5ByGameTypeOrderByCountOfDownloadsDesc(@Param("GameType") GameType gameType);
    List<ProductEntity> findTop5ByOrderByCreatedAtDesc();

    List<ProductEntity> findAllByAdvertisingTrueAndAccess(@Param("access") Access access);

    Page<ProductEntity> findAllByGameType(@Param("GameType") GameType gameType, Pageable pageable);
//    Page<ProductEntity> findAllByGameTypeAndPrice(@Param("GameType") GameType gameType,@Param("Price") Double price, Pageable pageable);
//    Page<ProductEntity> findAllByGameType(@Param("GameType") GameType gameType, Pageable pageable);
    Page<ProductEntity> findAllByGameTypeOrderByCreatedAtDesc(@Param("GameType") GameType gameType, Pageable pageable);
    Page<ProductEntity> findAllByGameTypeOrderByCountOfDownloadsDesc(@Param("GameType") GameType gameType, Pageable pageable);



    Page<ProductEntity> findAllByGameTypeAndCategories(@Param("GameType") GameType gameType, @Param("Categories") Categories categories, Pageable pageable);
    List<ProductEntity> findAllByAdvertisingTrueAndAccessAndGameType(@Param("access") Access access, @Param("GameType") GameType gameType);
    ProductEntity findByGdFileId(String fieldId);

    @Query("SELECT p from ProductEntity p where " +
    "LOWER(concat(p.title, p.shortInfo)) " +
    " LIKE %:searchQuery%")
    Page<ProductEntity> searchByInput(@Param("searchQuery") String searchQuery, Pageable pageable);
}

