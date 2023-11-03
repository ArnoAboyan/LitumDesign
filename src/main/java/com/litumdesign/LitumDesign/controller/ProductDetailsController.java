package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Access;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
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

    @GetMapping("/details/{productId}")
    public String findProductById(@PathVariable Long productId, Model model) {

       ProductEntity productDetails =  productEntityService.findProductDetailsEntityById(productId);

       if(productDetails.getAccess().equals(Access.PUBLIC)){
           model.addAttribute("productdetails", productDetails);

           productEntityService.viewsCounter(productDetails);
           return "productdetails";
       }return "errors/error-404";
    }


}
