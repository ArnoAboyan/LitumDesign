package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductEntityService {

    public final ProductEntityRepository productEntityRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createProductEntity(ProductEntity productEntity, List<String> photoLink, String gdFileId) {

//       GET LINKS FOR ProductEntity photos
        List<ProductPhotoEntity> productPhotos = new ArrayList<>();
        photoLink.forEach(a -> {
            ProductPhotoEntity productPhotoEntity = new ProductPhotoEntity(productEntity, a);
            productPhotos.add(productPhotoEntity);
        });

//        ADD PHOTO LINKS TO ProductEntity
        productEntity.setPhotoLink(productPhotos);

//        ADD FILE ID FROM GOOGLE DICK
        productEntity.setGdFileId(gdFileId);

//        GET USER FROM SESSION
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String username = authentication.getPrincipal().toString();
            System.out.println(username);


            Optional<UserEntity> optionalUserEntity = userRepository.findById(authentication.getName());


//            ADD UPLOAD USER USERNAME(ID)
            productEntity.setUploadUserId(optionalUserEntity.orElseThrow(() -> new NullPointerException("Active USER not found")));


            UserEntity userEntity = optionalUserEntity.orElseThrow(() -> new NullPointerException("Active USER not found"));
            userEntity.setCountOfUploads(userEntity.getCountOfUploads() + 1);

            userRepository.save(userEntity);


        } else {
            System.out.println("no user");
        }


        System.out.println(productEntity);

        productEntityRepository.save(productEntity);

    }

    public Page<ProductEntity> getAllProductEntity(Pageable pageable)  {
        Page<ProductEntity> products = productEntityRepository.findAll(pageable);


        if (products.getSize() != 20){
            log.error("Size is incorrect");
            throw new UnsupportedOperationException("Size is incorrect") {
            };
        }

        return products;
    }



    public List<ProductEntity> getMostPopularProduct() {
 List<ProductEntity> productEntities = productEntityRepository.findTop5ByOrderByCountOfDownloadsDesc();

        System.out.println("MostPopularProductEntity ->>>" + productEntities.toString());

        return productEntities;

    }

    public List<ProductEntity> getNewestProduct() {
        List<ProductEntity> productEntities = productEntityRepository.findTop5ByOrderByCreatedAtDesc();

        System.out.println("NewestProductEntity ->>>" + productEntities.toString());

        return productEntities;

    }

    public List<ProductEntity> getSliderProduct() {
        List<ProductEntity> productEntities = productEntityRepository.findAllByAdvertisingTrueAndAccess(Access.PUBLIC);

        System.out.println("SliderNewestProductEntity ->>>" + productEntities.toString());

        return productEntities;

    }

    public void downloadCounter(String fieldId){

        ProductEntity product = productEntityRepository.findByGdFileId(fieldId);

        product.setCountOfDownloads(product.getCountOfDownloads() + 1);

        productEntityRepository.save(product);
    }


    public Page<ProductEntity> getSearchResult( String searchQuery, Pageable pageable)  {
        Page<ProductEntity> products = productEntityRepository.searchByInput(searchQuery.toLowerCase(),pageable);


//        if (products.getSize() != 20){
//            log.error("Size is incorrect");
//            throw new UnsupportedOperationException("Size is incorrect") {
//            };
//        }

        return products;
    }

    public Page<ProductEntity> getAllProductByGameType( Pageable pageable)  {
        Page<ProductEntity> products = productEntityRepository.findAllByGameType(GameType.RUST, pageable);


//        if (products.getSize() != 20){
//            log.error("Size is incorrect");
//            throw new UnsupportedOperationException("Size is incorrect") {
//            };
//        }

        return products;
    }

    public List<ProductEntity> getSliderByGameType(GameType gameType) {
        List<ProductEntity> productEntities = productEntityRepository.findAllByAdvertisingTrueAndAccess(Access.PUBLIC);

        System.out.println("SliderNewestProductEntity ->>>" + productEntities.toString());

        return productEntities;

    }
}
