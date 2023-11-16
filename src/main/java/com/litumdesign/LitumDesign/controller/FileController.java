package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.formaticUI.Toast;
import com.litumdesign.LitumDesign.googledrive.GoogleDriveService;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.ProductPhotoService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
@SessionAttributes("photoLinkCounter")
@Log4j2
public class FileController {


    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;
    private final ProductPhotoService productPhotoService;

    private final GoogleDriveService googleDriveService;


    @ModelAttribute("photoLinkCounter")
    public AtomicInteger initializeCounter() {
        return new AtomicInteger(2);
    }

    @GetMapping("/addfile")
    public String submitFilePage(Model model) {

        AtomicInteger photoLinkCounter = (AtomicInteger) model.getAttribute("photoLinkCounter");
        Objects.requireNonNull(photoLinkCounter).set(2);


        return "submitfilepage";
    }


    @PostMapping("/addfile")
    @HxRequest
    public String addProductEntity(
            @RequestParam String title,
//                                   @RequestParam String titleImageLink,
////                                   @RequestParam Double price,
//                                   @RequestParam String shortInfo,
//                                   @RequestParam String license,
//                                   @RequestParam Access access,
//                                   @RequestParam String description,
//                                   @RequestParam String videoLink,
//                                   @RequestParam(value = "photoLink[]") List<String> photoLink,
//                                   @RequestParam Categories categories,
//                                   @RequestParam GameType gameType,
            @RequestParam String version,
            @RequestParam("uploadfile") MultipartFile uploadfile,
            Model model) {


        ProductEntity productEntity = new ProductEntity(
                title,
                null,
                0.0,
                null,
                null,
                null,
                null,
                null,
                Access.PRIVATE,
                null,
                false,
                0,
                0,
                0,
                0,
                0
        );

        try {
            String gdFileId = googleDriveService.uploadFile(uploadfile);
            productEntityService.createProductEntity(productEntity, gdFileId, version);


            model.addAttribute("uploadinfo", Toast.success("Success", "Product has bean created"));
            model.addAttribute("productEntity", productEntity.getId());
            return "fragments/success-upload-fragment";
        } catch (Exception e) {
            log.error("WE HAVE SOME PROBLEMS " + e.getMessage());
            model.addAttribute("uploadinfo", Toast.error("Error", "Failure occurred"));
            return "fragments/error-upload-fragment";
        }
    }

    @GetMapping("/add-photo-link-input")
    @HxRequest
    public String addOneMorePhotoLinkInput(Model model) {

        AtomicInteger photoLinkCounter = (AtomicInteger) model.getAttribute("photoLinkCounter");

        if (Objects.requireNonNull(photoLinkCounter).get() <= 100) {
            model.addAttribute("photolinkcounter", photoLinkCounter);
            photoLinkCounter.incrementAndGet();
            return "fragments/photoInputfragment";
        } else {
            model.addAttribute("toast", Toast.success("Success", "you can add no more than 15 photos"));
            return "fragments/toasts/errormessagefragment";
        }
    }

    @ResponseBody
    @DeleteMapping("/delete-photo-link-input")
    @HxRequest
    public String deletePhotoLinkInput(Model model) {

        AtomicInteger photoLinkCounter = (AtomicInteger) model.getAttribute("photoLinkCounter");
        Objects.requireNonNull(photoLinkCounter).decrementAndGet();
        return "";
    }

    @ResponseBody
    @DeleteMapping("/delete-product")
    @HxRequest
    public String deleteProduct(@RequestParam("productId") Long productId, @AuthenticationPrincipal UserDetails userDetails) {

        ProductEntity productEntity = productEntityService.findProductEntityById(productId);

        if (productEntity.getUploadUserId().getLogin().equals(userDetails.getUsername()))
            userEntityService.deleteUploadCounter(userDetails);
        productEntityService.deleteProductEntity(productId);

        return "";
    }


    @GetMapping("/download-file/{productId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String productId, @AuthenticationPrincipal UserDetails userDetails) throws GeneralSecurityException, IOException {


        ProductEntity productEntity = productEntityService.findProductEntityById(Long.parseLong(productId));

//        ProductEntity productEntity = productEntityService.findByGdFileId(fileId);

        if (productEntity.getAccess().equals(Access.PUBLIC)) {
            productEntityService.downloadCounter(productEntity);
            if (userDetails != null) {
                userEntityService.userDownloadCounter(userDetails);
            }

//            return googleDriveService.downloadFile(fileId);
            return googleDriveService.downloadFile(productEntity.getGdFileId());
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @GetMapping("/search")
    @HxRequest
    public String addProductEntity(@RequestParam String searchquery,
                                   @PageableDefault(size = 20) Pageable pageable,
                                   Model model) {

        model.addAttribute("resultProducts", productEntityService.getSearchResult(searchquery, pageable));
        model.addAttribute("products", productEntityService.getMostPopularProduct());


        return "fragments/searchresultfragment";
    }


    @GetMapping("/updatefile/{productEntityId}")
    public String updateFilePage(@PathVariable Long productEntityId, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        ProductEntity productEntity = productEntityService.findProductEntityById(productEntityId);

        if (userDetails.getUsername().equals(productEntity.getUploadUserId().getLogin())) {

            AtomicInteger photoLinkCounter = (AtomicInteger) model.getAttribute("photoLinkCounter");
            Objects.requireNonNull(photoLinkCounter).set(2);


            model.addAttribute("actualProductEntity", productEntity);
            model.addAttribute("lastVersion", Integer.parseInt(productEntityService.findLastVersionByProductEntity(productEntity).getVersion()) + 1);

            return "updatefilepage";
        }
        return "errors/error-403";
    }

    @PostMapping("/updatefile")
    @HxRequest
    public String updateProductEntity(
            @RequestParam Long productEntityId,
            @RequestParam String title,
            @RequestParam String titleImageLink,
//                                 @RequestParam Double price,
            @RequestParam String shortInfo,
            @RequestParam String license,
            @RequestParam Access access,
            @RequestParam String description,
            @RequestParam String videoLink,
            @RequestParam Categories categories,
            @RequestParam GameType gameType,
            @RequestParam String version,
            @RequestParam String versionComment,
            Model model) {


        try {
            productEntityService.updateProductEntity(productEntityId, title,
                    titleImageLink,
                    shortInfo,
                    license,
                    access,
                    description,
                    videoLink,
                    categories,
                    gameType,
                    version,
                    versionComment);


            model.addAttribute("uploadinfo", Toast.success("Success", "Product has bean updated"));
            model.addAttribute("productEntity", productEntityId);
            return "fragments/success-upload-fragment";
        } catch (Exception e) {
            log.error("WE HAVE SOME PROBLEMS " + e.getMessage());
            model.addAttribute("uploadinfo", Toast.error("Error", "Failure occurred"));
            return "fragments/error-upload-fragment";
        }
    }


    @PostMapping("/addProductPhotos")
    @HxRequest
    public String uploadProductPhotos(
            @RequestParam Long productEntityId,
            @RequestParam(value = "uploadPhotos") List<MultipartFile> photo, Model model,
            RedirectAttributes redirectAttributes) {

        ProductEntity productEntity = productEntityService.findProductEntityById(productEntityId);
        model.addAttribute("actualProductEntity", productEntity);

        productEntityService.uploadProductPhotos(productEntityId, photo);

        model.addAttribute("message", "Images has been upload success!");

        return "fragments/images-product-fragment";
    }

    @DeleteMapping("/delete-image")
    @HxRequest
    public String deleteProductImage(
            @RequestParam("photoId") Long photoId,
            @RequestParam("productId") Long productId,
            @AuthenticationPrincipal UserDetails userDetails, Model model) {

        ProductEntity productEntity = productEntityService.findProductEntityById(productId);

        if (productEntity.getUploadUserId().getLogin().equals(userDetails.getUsername()))

            productPhotoService.deletePhotoEntity(photoId);

        model.addAttribute("actualProductEntity", productEntity);

        return "fragments/images-product-fragment";
    }

}