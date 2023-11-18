package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.googledrive.GoogleDriveService;
import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
import com.litumdesign.LitumDesign.repository.ProductVersionRepository;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductEntityService {

    private final ProductEntityRepository productEntityRepository;
    private final UserRepository userRepository;
    private final ProductVersionRepository productVersionRepository;
    private final GoogleDriveService googleDriveService;



    @Transactional
    public void createProductEntity(ProductEntity productEntity, String gdFileId, String version) {

//        delete empty cells
//        cleanEmptyCell(photoLink);
//        photoLink.removeIf(item -> item == null || item.isEmpty());


////       CREATE EMPTY PHOTOS ArrayList
//        List<ProductPhotoEntity> emptyProductPhotos = new ArrayList<>();
////        ADD PHOTO LINKS TO ProductEntity
//        productEntity.setPhotoLink(emptyProductPhotos);


//        ADD FILE ID FROM GOOGLE DICK
        productEntity.setGdFileId(gdFileId);


        List<ProductVersionEntity> productVersion = new ArrayList<>();
        productVersion.add(new ProductVersionEntity(productEntity, version, "Release"));
        productEntity.setProductVersion(productVersion);

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

    public Page<ProductEntity> getAllProductEntity(Pageable pageable) {
        Page<ProductEntity> products = productEntityRepository.findAllByAccess(pageable, Access.PUBLIC);


        if (products.getSize() != 20) {
            log.error("Size is incorrect");
            throw new UnsupportedOperationException("Size is incorrect") {
            };
        }

        return products;
    }


    public List<ProductEntity> getMostPopularProduct() {


        return productEntityRepository.findTop5ByAccessOrderByCountOfDownloadsDesc(Access.PUBLIC);

    }

    public List<ProductEntity> getMostPopularProductByVendor(String name) {


        return productEntityRepository.findTop5ByUploadUserIdNameAndAccessOrderByCountOfDownloadsDesc(name, Access.PUBLIC);

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

    public void downloadCounter(ProductEntity product) {

//        ProductEntity product = productEntityRepository.findByGdFileId(fieldId);

        product.setCountOfDownloads(product.getCountOfDownloads() + 1);

        productEntityRepository.save(product);
    }

    public void viewsCounter(ProductEntity productEntity) {

        productEntity.setCountOfView(productEntity.getCountOfView() + 1);

        productEntityRepository.save(productEntity);
    }

    public Page<ProductEntity> getSearchResult(String searchQuery, Pageable pageable) {


        return productEntityRepository.searchByInput(searchQuery.toLowerCase(), pageable);
    }

    public Page<ProductEntity> getSearchResultByVendor(String searchQuery, Pageable pageable, String vendorName) {


        return productEntityRepository.searchByInputAndVendor(searchQuery.toLowerCase(), vendorName, pageable);
    }

    public List<ProductEntity> getSearchResultForVendors(String searchQuery, UserEntity userEntity) {


        return productEntityRepository.searchByInputAndUploadUserId(searchQuery.toLowerCase(), userEntity);
    }

    public Page<ProductEntity> getAllProductByGameTypeAndSort(GameType gameType, Pageable pageable) {


        return productEntityRepository.findAllByGameTypeAndAccess(gameType, pageable, Access.PUBLIC);
    }


    public Page<ProductEntity> getAllProductByGameTypeAndCategoriesAndSort(GameType gameType, Categories categories, Pageable pageable) {


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

    public ProductEntity findProductEntityById(Long productId) {

        return productEntityRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not find"));

    }



    public ProductVersionEntity findLastVersionByProductEntity(ProductEntity productEntity) {
        List<ProductVersionEntity> productVersionEntityList = productVersionRepository.findProductVersionEntitiesByProductEntityOrderByVersion(productEntity);

        return productVersionEntityList.get(productVersionEntityList.size() - 1);
    }


    public ProductEntity findByGdFileId(String gdFileId) {
        return productEntityRepository.findByGdFileId(gdFileId);
    }

    public List<ProductEntity> findAll() {

        return productEntityRepository.findAll();
    }

    public List<ProductEntity> findAllByVendorId(UserEntity uploadVendorId) {

        return productEntityRepository.findByUploadUserId(uploadVendorId);
    }

    public Page<ProductEntity> findAllProductsByVendorName(String name, Pageable pageable) {

        return productEntityRepository.findByUploadUserIdNameAndAccess(name, pageable, Access.PUBLIC);
    }


    public List<ProductEntity> findByVendorIdAndCategories(UserEntity uploadVendorId, Categories categories) {

        return productEntityRepository.findByUploadUserIdAndCategories(uploadVendorId, categories);
    }

    public int sumOfDownloads(List<ProductEntity> productEntityList) {

        return productEntityList.stream()
                .mapToInt(ProductEntity::getCountOfDownloads)
                .sum();

    }

    public int sumOfViews(List<ProductEntity> productEntityList) {

        return productEntityList.stream()
                .mapToInt(ProductEntity::getCountOfView)
                .sum();

    }

    public void cleanEmptyCell(List<MultipartFile> photoLink) {
        photoLink.removeIf(item -> item == null || item.isEmpty());
    }


    public void updateProductEntity(Long productEntityId,
                                    String title,
                                    String titleImageLink,
                                    String shortInfo,
                                    String license,
                                    Access access,
                                    String description,
                                    String videoLink,
                                    Categories categories,
                                    GameType gameType,
                                    String version,
                                    String versionComment) {


        ProductEntity productEntity = findProductEntityById(productEntityId);


        if (title != null && !title.isEmpty()) {
            productEntity.setTitle(title);
        }

        if (titleImageLink != null && !titleImageLink.isEmpty()) {
            productEntity.setTitleImageLink(titleImageLink);
        }

        if (shortInfo != null && !shortInfo.isEmpty()) {
            productEntity.setShortInfo(shortInfo);
        }

//           DELETE OLD PHOTOS
//        productPhotoRepository.deleteByProductEntity(productEntity);


        if (license != null && !license.isEmpty()) {
            productEntity.setLicense(license);
        }

        productEntity.setAccess(access);

        if (description != null && !description.isEmpty()) {
            productEntity.setDescription(description);
        }
        if (videoLink != null && !videoLink.isEmpty()) {
            productEntity.setVideoLink(videoLink);
        }

        productEntity.setCategories(categories);
        productEntity.setGameType(gameType);


        List<ProductVersionEntity> productVersion = productEntity.getProductVersion();
        if (!versionComment.isEmpty()) {
            productVersion.add(new ProductVersionEntity(productEntity, version, versionComment));
            productEntity.setProductVersion(productVersion);
        }


        System.out.println(productEntity);

        productEntityRepository.save(productEntity);

    }

    public void checkMIMEType(List<MultipartFile> photoLink) {
        photoLink.removeIf(item -> !Objects.requireNonNull(item.getContentType()).startsWith("image"));
    }

    public void uploadProductPhotos(Long productEntityId, List<MultipartFile> photos) {

        ProductEntity productEntity = findProductEntityById(productEntityId);

        //        delete empty cells
        cleanEmptyCell(photos);
        checkMIMEType(photos);


        List<String> photoIds = googleDriveService.uploadProductPhotos(photos);


        //       GET LINKS FOR ProductEntity photos
        if (photoIds != null) {
            List<ProductPhotoEntity> productPhotos = productEntity.getPhotoLink();
            photoIds.forEach(a -> {
                ProductPhotoEntity productPhotoEntity = new ProductPhotoEntity(productEntity, a);
                productPhotos.add(productPhotoEntity);
            });
//        ADD PHOTO LINKS TO ProductEntity
            productEntity.setPhotoLink(productPhotos);
        }
        productEntityRepository.save(productEntity);

    }


    public void uploadMainProductPhotos(Long productEntityId, MultipartFile photo) {

        ProductEntity productEntity = findProductEntityById(productEntityId);

      ProductEntity newProductEntity = googleDriveService.uploadMainProductPhotos(productEntity, photo);

      productEntityRepository.save(newProductEntity);
    }

    @Transactional
    public void deleteProductEntity(Long productEntityId) {

        ProductEntity productEntity = findProductEntityById(productEntityId);
        googleDriveService.deleteFile(productEntity.getGdFileId());
        productEntityRepository.deleteById(productEntityId);
        System.out.println("DELETE PRODUCT -->" + productEntityId + " and " + productEntity.getGdFileId());
    }

    public void deleteMainPhoto(ProductEntity productEntity) {

        try {
            googleDriveService.deleteFile(productEntity.getTitleImageLink());
            productEntity.setTitleImageLink("");
            productEntity.setTitleImageThumbnails("");
            log.info("Image for " + productEntity.getTitle() + " has been delete successful" );
            productEntityRepository.save(productEntity);
        }catch (Exception e){
            log.error("Error while deleting main product image " + e);
        }


    }
}

