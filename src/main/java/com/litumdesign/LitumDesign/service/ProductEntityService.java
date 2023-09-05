package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.ProductPhotoEntity;
import com.litumdesign.LitumDesign.Entity.UserEntity;
import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ProductEntityService {

    public final ProductEntityRepository productEntityRepository;
    private final UserRepository userRepository;

    public void createProductEntity (ProductEntity productEntity, List<String> photoLink){

//       GET LINKS FOR ProductEntity photos
        List<ProductPhotoEntity> productPhotos = new ArrayList<>();
        photoLink.forEach(a -> {
            ProductPhotoEntity productPhotoEntity = new ProductPhotoEntity(productEntity, a);
            productPhotos.add(productPhotoEntity);
        });

//        ADD PHOTO LINKS TO ProductEntity
        productEntity.setPhotoLink(productPhotos);


//        GET USER FROM SESSION
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String username = authentication.getPrincipal().toString();
            System.out.println(username);


            Optional<UserEntity> userEntity = userRepository.findById(authentication.getName());
            System.out.println(userRepository.findById(authentication.getName()));

//            ADD UPLOAD USER USERNAME(ID)
            productEntity.setUploadUserId(userEntity.orElseThrow(() -> new NullPointerException("Active USER not found")));

        } else {
            System.out.println("no user");
        }


        System.out.println(productEntity);



        productEntityRepository.save(productEntity);







    }

}
