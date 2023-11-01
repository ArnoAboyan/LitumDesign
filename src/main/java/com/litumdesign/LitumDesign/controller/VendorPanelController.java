package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vendor")
public class VendorPanelController {

    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;


    @GetMapping("/vendorpanel/{vendorid}")
    public String openVendorPanel(@PathVariable String vendorid, Model model, @AuthenticationPrincipal UserDetails userDetails ){

        if (userDetails.getUsername().equalsIgnoreCase(vendorid)){
            userEntityService.getUserById(vendorid);

            List<ProductEntity> productEntityList = productEntityService.findAllByVendorId(userEntityService.getUserById(vendorid));

            productEntityService.sumOfDownloads(productEntityList);


            model.addAttribute("allvendorproducts", productEntityList);
            model.addAttribute("sumofdownloads", productEntityService.sumOfDownloads(productEntityList));
            model.addAttribute("sumofviews", productEntityService.sumOfViews(productEntityList));

            return "vendorpanel";
        }return "errors/error-404";
    }
}
