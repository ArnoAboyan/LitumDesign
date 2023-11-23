package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Access;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductDetailsController {

    private final ProductEntityService productEntityService;

    @GetMapping("/details/{productId}/{productName}")
    public String findProductById(@PathVariable Long productId,
                                  Model model, @AuthenticationPrincipal UserDetails userDetails) {

       ProductEntity productDetails =  productEntityService.findProductEntityById(productId);

       if(productDetails.getAccess().equals(Access.PUBLIC)){
           model.addAttribute("productdetails", productDetails);

           productEntityService.viewsCounter(productDetails);
           return "productdetails";
       } else if (userDetails.getUsername() !=null && userDetails.getUsername().equals(productDetails.getUploadUserId().getLogin())) {
           model.addAttribute("productdetails", productDetails);
           productEntityService.viewsCounter(productDetails);
           return "productdetails";
       }else return "errors/error-404";
    }


}
