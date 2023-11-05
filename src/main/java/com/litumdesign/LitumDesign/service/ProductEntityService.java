package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
//import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductEntityService {

    public final ProductEntityRepository productEntityRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createProductEntity(ProductEntity productEntity, List<String> photoLink, String gdFileId) {

//        delete empty cells
        photoLink.removeIf(item -> item == null || item.isEmpty());


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
        Page<ProductEntity> products = productEntityRepository.findAllByAccess(pageable, Access.PUBLIC );


        if (products.getSize() != 20){
            log.error("Size is incorrect");
            throw new UnsupportedOperationException("Size is incorrect") {
            };
        }

        return products;
    }



    public List<ProductEntity> getMostPopularProduct() {


        return productEntityRepository.findTop5ByAccessOrderByCountOfDownloadsDesc(Access.PUBLIC);

    }


    public List<ProductEntity> getMostPopularProductWithGameType(GameType gameType) {


        return productEntityRepository.findTop5ByGameTypeAndAccessOrderByCountOfDownloadsDesc(gameType, Access.PUBLIC);

    }

    public List<ProductEntity> getNewestProduct() {


        return productEntityRepository.findTop5ByAccessOrderByCreatedAtDesc(Access.PUBLIC);

    }

    public List<ProductEntity> getSliderProduct() {


        return productEntityRepository.findAllByAdvertisingTrueAndAccess(Access.PUBLIC);

    }

    public void downloadCounter(ProductEntity product){

//        ProductEntity product = productEntityRepository.findByGdFileId(fieldId);

        product.setCountOfDownloads(product.getCountOfDownloads() + 1);

        productEntityRepository.save(product);
    }

    public void viewsCounter(ProductEntity productEntity){

        productEntity.setCountOfView(productEntity.getCountOfView() + 1);

        productEntityRepository.save(productEntity);
    }

    public Page<ProductEntity> getSearchResult( String searchQuery, Pageable pageable)  {


        return productEntityRepository.searchByInput(searchQuery.toLowerCase(),pageable);
    }

    public List<ProductEntity> getSearchResultForVendors(String searchQuery, UserEntity userEntity)  {


        return productEntityRepository.searchByInputAndUploadUserId(searchQuery.toLowerCase(), userEntity);
    }

    public Page<ProductEntity> getAllProductByGameTypeAndSort(GameType gameType, Pageable pageable)  {


        return productEntityRepository.findAllByGameTypeAndAccess(gameType, pageable, Access.PUBLIC);
    }



    public Page<ProductEntity> getAllProductByGameTypeAndCategoriesAndSort(GameType gameType, Categories categories, Pageable pageable )  {


        return productEntityRepository.findAllByGameTypeAndCategoriesAndAccess(gameType, categories, pageable, Access.PUBLIC);
    }

//    public List<ProductEntity> getSliderByGameType(GameType gameType) {
//        List<ProductEntity> productEntities = productEntityRepository.findAllByAdvertisingTrueAndAccess(Access.PUBLIC);
//
//        System.out.println("SliderNewestProductEntity ->>>" + productEntities.toString());
//
//        return productEntities;
//
//    }

    public ProductEntity findProductDetailsEntityById(Long productId){

        return productEntityRepository.findById(productId).orElseThrow(()-> new NullPointerException("Product not find"));

    }

    public ProductEntity findByGdFileId (String gdFileId){
        return productEntityRepository.findByGdFileId(gdFileId);
    }

    public List<ProductEntity> findAllByVendorId (UserEntity uploadVendorId)  {

        return productEntityRepository.findByUploadUserId(uploadVendorId);
    }


    public List<ProductEntity> findByVendorIdAndCategories (UserEntity uploadVendorId, Categories categories)  {

        return productEntityRepository.findByUploadUserIdAndCategories(uploadVendorId, categories);
    }

    public int sumOfDownloads(List<ProductEntity> productEntityList){

        return productEntityList.stream()
                .mapToInt(ProductEntity::getCountOfDownloads)
                .sum();

    }

    public int sumOfViews(List<ProductEntity> productEntityList){

        return productEntityList.stream()
                .mapToInt(ProductEntity::getCountOfView)
                .sum();

    }

}
