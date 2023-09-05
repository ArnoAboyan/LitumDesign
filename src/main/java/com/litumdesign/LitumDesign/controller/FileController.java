package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.googledrive.GoogleDriveService;
import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
import com.litumdesign.LitumDesign.repository.ProductPhotoRepository;
import com.litumdesign.LitumDesign.repository.TestClassRepository;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/gtest")
    public String gTest() throws GeneralSecurityException, IOException {

        GoogleDriveService.googleDriveAction();
        return "index";
    }

    @PostMapping("/addfile")
    public String addProductEntity(@RequestParam String title,
                                   @RequestParam int price,
                                   @RequestParam String shopInfo,
                                   @RequestParam String shortInfo,
                                   @RequestParam String description,
                                   @RequestParam String videoLink,
                                   @RequestParam (value="photoLink[]") List<String> photoLink,
                                   @RequestParam Categories categories,
                                   @RequestParam GameType gameType,
                                   @RequestParam("uploadfile") MultipartFile file,
                                   Model model){

        ProductEntity productEntity = new ProductEntity(
                title,
                price,
                shopInfo,
                shortInfo,
                description,
                categories,
                gameType,
                videoLink
        );

        productEntityService.createProductEntity(productEntity, photoLink);





            try {
                model.addAttribute("testClass", productEntityRepository.findAll().toString());
                model.addAttribute("correctorresp", "User has bean created");

                System.out.println(  "UPLOAD FILE---->>" + file.toString());
                System.out.println(  "UPLOAD FILE---->>" + file.getName());
                System.out.println(  "UPLOAD FILE---->>" + file.getContentType());
                System.out.println(  "UPLOAD FILE---->>" + file.getOriginalFilename());
                System.out.println(  "UPLOAD FILE---->>" + file.getResource());
                System.out.println(  "UPLOAD FILE---->>" + file.getInputStream());
                System.out.println(  "UPLOAD FILE---->>" + file.getSize());
               googleDriveService.uploadFile(file);

            } catch (Exception e) {

                System.out.println("WE HAVE SOME PROBLEMS " + e.getMessage());
            }

            return "alltestusers"; // Имя вашего представленияa
        }

}