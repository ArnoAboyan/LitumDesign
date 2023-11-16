package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.ProductPhotoEntity;
import com.litumdesign.LitumDesign.googledrive.GoogleDriveService;
import com.litumdesign.LitumDesign.repository.ProductPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductPhotoService {

    private final ProductPhotoRepository productPhotoRepository;
    private final GoogleDriveService googleDriveService;



    public void deletePhotoEntity(Long photoEntityId) {
        ProductPhotoEntity productPhotoEntity = productPhotoRepository.findById(photoEntityId).orElseThrow(()-> new NullPointerException("Error! Photo not found!"));
        productPhotoRepository.deleteById(photoEntityId);
        googleDriveService.deleteFile(productPhotoEntity.getPhotoLink());

    }
}
