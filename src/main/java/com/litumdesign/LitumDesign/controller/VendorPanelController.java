package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Categories;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vendor")
public class VendorPanelController {

    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;


    @GetMapping("/vendorpanel")
    public String openVendorPanel(Model model, @AuthenticationPrincipal UserDetails userDetails ){

        if (userDetails.getUsername() != null){

            List<ProductEntity> productEntityList = productEntityService.findAllByVendorId(userEntityService.getUserById(userDetails.getUsername()));

            productEntityService.sumOfDownloads(productEntityList);


            model.addAttribute("allvendorproducts", productEntityList);
            model.addAttribute("sumofdownloads", productEntityService.sumOfDownloads(productEntityList));
            model.addAttribute("sumofviews", productEntityService.sumOfViews(productEntityList));

            return "vendorpanel";
        }return "errors/error-404";
    }



    @GetMapping("/vendorpanel/bycategory/{category}")
    @HxRequest
    public String getProductByCategoryHx(@PathVariable Categories category, Model model, @AuthenticationPrincipal UserDetails userDetails ){

        System.out.println("SUCCESS" + category);

            List<ProductEntity> productEntityList =  productEntityService.findByVendorIdAndCategories(userEntityService.getUserById(userDetails.getUsername()), category);
        model.addAttribute("vendorproductsbycategory", productEntityList);

        return "fragments/vendorpanelproductsfragment";
    }

    @GetMapping("vendorpanel/allproducts")
    @HxRequest
    public String getAllProductHx(Model model, @AuthenticationPrincipal UserDetails userDetails ){

        List<ProductEntity> productEntityList = productEntityService.findAllByVendorId(userEntityService.getUserById(userDetails.getUsername()));
        model.addAttribute("vendorproductsbycategory", productEntityList);

        return "fragments/vendorpanelproductsfragment";
    }

    @GetMapping("vendorpanel/search")
    @HxRequest
    public String findProductHx(@RequestParam String searchquery, Model model, @AuthenticationPrincipal UserDetails userDetails ){

        List<ProductEntity> productEntityList = productEntityService.getSearchResultForVendors(searchquery, userEntityService.getUserById(userDetails.getUsername()));
        model.addAttribute("vendorproductsbycategory", productEntityList);

        return "fragments/vendorpanelproductsfragment";
    }

}
