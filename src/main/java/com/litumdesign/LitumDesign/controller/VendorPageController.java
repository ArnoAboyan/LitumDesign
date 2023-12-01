package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Role;
import com.litumdesign.LitumDesign.Entity.UserEntity;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/vendor-page")
@Log4j2
public class VendorPageController {

    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;

    @GetMapping("/{vendorName}")
    public String openVendorPanel(
            @PathVariable("vendorName") String vendorName,
            Model model,
            @PageableDefault(size = 20) Pageable pageable,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userEntity = userEntityService.getUserByName(vendorName);


            if(!userEntity.getAuthorities().equals(Role.VENDOR) && !userEntity.getAuthorities().equals(Role.ADMIN)){
                return "errors/error-404";
            } try {
            Sort sort = pageable.getSort();
            List<Sort.Order> orders = sort.toList();

            for (Sort.Order order : orders) {
                String sortName = order.getProperty();
                model.addAttribute("sortName", sortName);
            }


            model.addAttribute("vendor", userEntity );
            model.addAttribute("allProducts", productEntityService.findAllProductsByVendorName(vendorName, pageable));
            model.addAttribute("products", productEntityService.getMostPopularProductByVendor(vendorName));
            if (userDetails != null && userEntity.getLogin().equals(userDetails.getUsername())){
                return "editable-vendor-page";
            }

            return "vendor-page";

        } catch (PropertyReferenceException e) {

            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error! Wrong request for sorting");

            return "redirect:/vendor-page/" + vendorName;
        }
    }

    @PostMapping("/edit-social")
    @HxRequest
    public String editSocialLinks(
            @RequestParam String discord,
            @RequestParam String telegram,
            @RequestParam String twitter,
            @RequestParam String facebook,
            @RequestParam String linkedIn,
            @RequestParam String youTube,
            @RequestParam String vendorName,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails){

        UserEntity userEntity = userEntityService.getUserById(userDetails.getUsername());

        try{
            if (vendorName.equals(userEntity.getName())) {

                userEntityService.setUserSocialLinks(discord, telegram, twitter, facebook, linkedIn, youTube, userEntity);
                model.addAttribute("vendor", userEntity );
                model.addAttribute("message", "Changes applied successfully!");

            }

        }catch (Exception e){
            log.error("Error while editing social links " + e);
            model.addAttribute("error", "Error! No changes applied!");

        }

        return  "fragments/editable-vendor-page-fragment";
    }

    @PostMapping("/edit-avatar")
    @HxRequest
    public String editAvatarLinks(
            @RequestParam(value = "uploadAvatar") MultipartFile avatar,
            @RequestParam String vendorName,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails){

        UserEntity userEntity = userEntityService.getUserById(userDetails.getUsername());

        try{
            if (vendorName.equals(userEntity.getName())) {

                 userEntityService.uploadUserAvatar(userEntity, avatar);

                model.addAttribute("vendor", userEntity);
                model.addAttribute("message", "Avatar has been upload success!");

            }
        }catch (Exception e){
        log.error("Error while adding avatar" + e);
        model.addAttribute("error", "Error while adding avatar!");
    }
        return  "fragments/editable-vendor-page-fragment";
    }


    @PostMapping("/edit-banner")
    @HxRequest
    public String editBannerLinks(
            @RequestParam(value = "uploadBanner") MultipartFile banner,
            @RequestParam String vendorName,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails){

        UserEntity userEntity = userEntityService.getUserById(userDetails.getUsername());

        try{
            if (vendorName.equals(userEntity.getName())) {

                 userEntityService.uploadUserBanner(userEntity, banner);

                model.addAttribute("vendor", userEntity);
                model.addAttribute("message", "Banner has been upload successfully!");

            }
        }catch (Exception e){
        log.error("Error while adding banner" + e);
        model.addAttribute("error", "Error while adding banner!");
    }
        return  "fragments/editable-vendor-page-fragment";
    }


    @GetMapping("/search")
    @HxRequest
    public String addProductEntity(@RequestParam String searchquery,
                                   @RequestParam String vendorname,
                                   @PageableDefault(size = 20) Pageable pageable,
                                   Model model) {

        model.addAttribute("allProducts", productEntityService.getSearchResultByVendor(searchquery, pageable,vendorname));
        model.addAttribute("products", productEntityService.getMostPopularProductByVendor(vendorname));
        return "fragments/allproductsfragment";
    }

}


