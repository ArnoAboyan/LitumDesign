package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findAllByAccess(Pageable pageable, @Param("access") Access access);

    List<ProductEntity> findTop5ByAccessOrderByCountOfDownloadsDesc(@Param("access") Access access);
    List<ProductEntity> findTop5ByGameTypeAndAccessOrderByCountOfDownloadsDesc(@Param("GameType") GameType gameType, @Param("access") Access access);
    List<ProductEntity> findTop5ByAccessOrderByCreatedAtDesc(@Param("access") Access access);

    List<ProductEntity> findAllByAdvertisingTrueAndAccess(@Param("access") Access access);

    Page<ProductEntity> findAllByGameTypeAndAccess(@Param("GameType") GameType gameType, Pageable pageable,@Param("access") Access access);
//    Page<ProductEntity> findAllByGameTypeOrderByCreatedAtDesc(@Param("GameType") GameType gameType, Pageable pageable);
//    Page<ProductEntity> findAllByGameTypeOrderByCountOfDownloadsDesc(@Param("GameType") GameType gameType, Pageable pageable);



    Page<ProductEntity> findAllByGameTypeAndCategoriesAndAccess(@Param("GameType") GameType gameType, @Param("Categories") Categories categories, Pageable pageable, @Param("access") Access access);
//    List<ProductEntity> findAllByAdvertisingTrueAndAccessAndGameType(@Param("access") Access access, @Param("GameType") GameType gameType);
    ProductEntity findByGdFileId(String fieldId);

    @Query("SELECT p from ProductEntity p where " +
    "LOWER(concat(p.title, p.shortInfo)) " +
    " LIKE %:searchQuery%" + "AND p.access = 'PUBLIC'")
    Page<ProductEntity> searchByInput(@Param("searchQuery") String searchQuery, Pageable pageable);

    List<ProductEntity> findByUploadUserId(@Param("UploadUserId") UserEntity userEntity);

    List<ProductEntity> findByUploadUserIdAndCategories(@Param("UploadUserId") UserEntity userEntity, @Param("Categories") Categories categories);

}

