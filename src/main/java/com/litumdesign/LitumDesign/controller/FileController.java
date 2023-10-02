package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.googledrive.GoogleDriveService;
import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
import com.litumdesign.LitumDesign.repository.ProductPhotoRepository;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final ProductEntityRepository productEntityRepository;
    private final ProductPhotoRepository productPhotoRepository;

    private final ProductEntityService productEntityService;

    private final GoogleDriveService googleDriveService;

    @GetMapping("/addfile")
    public String submitFilePage() {
        return "submitfilepage";
    }


    @PostMapping("/addfile")
    public String addProductEntity(@RequestParam String title,
                                   @RequestParam String titleImageLink,
                                   @RequestParam Double price,
                                   @RequestParam String shortInfo,
                                   @RequestParam Access access,
                                   @RequestParam String description,
                                   @RequestParam String videoLink,
                                   @RequestParam(value = "photoLink[]") List<String> photoLink,
                                   @RequestParam Categories categories,
                                   @RequestParam GameType gameType,
                                   @RequestParam("uploadfile") MultipartFile file,
                                   @PageableDefault(size = 20) Pageable pageable,
                                   Model model) throws GeneralSecurityException, IOException {

        ProductEntity productEntity = new ProductEntity(
                title,
                titleImageLink,
                price,
                shortInfo,
                description,
                categories,
                gameType,
                access,
                videoLink,
                false
        );

//        String gdFileId = googleDriveService.uploadFile(file);
//        System.out.println("GDFILEID ->>>" + gdFileId);
//        productEntityService.createProductEntity(productEntity, photoLink, gdFileId);

        try {
            String gdFileId = googleDriveService.uploadFile(file);
            System.out.println("GDFILEID ->>>" + gdFileId);
            productEntityService.createProductEntity(productEntity, photoLink, gdFileId);

            model.addAttribute("products", productEntityService.getMostPopularProduct());
            model.addAttribute("productsNewest", productEntityService.getNewestProduct());
            model.addAttribute("productsSlider", productEntityService.getSliderProduct());
            model.addAttribute("allProducts", productEntityService.getAllProductEntity(pageable));

            model.addAttribute("correctorresp", "Product has bean created");

        } catch (Exception e) {

            System.out.println("WE HAVE SOME PROBLEMS " + e.getMessage());
        }

        return "index";
    }



    @GetMapping("/download-file/{fileId}")
    @Async
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws GeneralSecurityException, IOException {
        productEntityService.downloadCounter(fileId);
        return googleDriveService.downloadFile(fileId);
    }




}

