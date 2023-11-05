package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.formaticUI.Toast;
import com.litumdesign.LitumDesign.googledrive.GoogleDriveService;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
@SessionAttributes("photoLinkCounter")
public class FileController {


    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;

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
    public String addProductEntity(@RequestParam String title,
                                   @RequestParam String titleImageLink,
//                                   @RequestParam Double price,
                                   @RequestParam String shortInfo,
                                   @RequestParam String license,
                                   @RequestParam Access access,
                                   @RequestParam String description,
                                   @RequestParam String videoLink,
                                   @RequestParam(value = "photoLink[]") List<String> photoLink,
                                   @RequestParam Categories categories,
                                   @RequestParam GameType gameType,
                                   @RequestParam String version,
                                   @RequestParam("uploadfile") MultipartFile uploadfile,
                                   @PageableDefault(size = 20) Pageable pageable,
                                   Model model) {

        System.out.println("UPLOAD FILE -->>" + uploadfile);

        ProductEntity productEntity = new ProductEntity(
                title,
                titleImageLink,
                0.0,
                shortInfo,
                license,
                description,
                categories,
                gameType,
                access,
                videoLink,
                false,
                0,
                0,
                0,
                0,
                0
        );

        try {
            String gdFileId = googleDriveService.uploadFile(uploadfile);
            System.out.println("GDFILEID ->>>" + gdFileId);
            productEntityService.createProductEntity(productEntity, photoLink, gdFileId, version);

            model.addAttribute("products", productEntityService.getMostPopularProduct());
            model.addAttribute("productsNewest", productEntityService.getNewestProduct());
            model.addAttribute("productsSlider", productEntityService.getSliderProduct());
            model.addAttribute("allProducts", productEntityService.getAllProductEntity(pageable));

            model.addAttribute("correctorresp", "Product has bean created");

        } catch (Exception e) {
            System.out.println("WE HAVE SOME PROBLEMS " + e.getMessage());

        }
        System.out.println("UPLOAD FILE -->>" + uploadfile);
        return "fragments/successfragment";
    }

    @GetMapping("/add-photo-link-input")
    @HxRequest
    public String addOneMorePhotoLinkInput(Model model) {

        AtomicInteger photoLinkCounter = (AtomicInteger) model.getAttribute("photoLinkCounter");


            System.out.println("photoLinkCounter --->" + Objects.requireNonNull(photoLinkCounter).get());



        if (Objects.requireNonNull(photoLinkCounter).get() <= 15) {
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
        System.out.println("photoLinkCounter --->" + photoLinkCounter.get());
        return "";
    }

    @GetMapping("/download-file/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, @AuthenticationPrincipal UserDetails userDetails) throws GeneralSecurityException, IOException {

        ProductEntity productEntity = productEntityService.findByGdFileId(fileId);

        if (productEntity.getAccess().equals(Access.PUBLIC)) {
            productEntityService.downloadCounter(productEntity);
            if (userDetails != null) {
                userEntityService.userDownloadCounter(userDetails);
            }
            return googleDriveService.downloadFile(fileId);
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


}

