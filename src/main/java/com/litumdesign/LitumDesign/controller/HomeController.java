package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductEntityService productEntityService;

    @GetMapping
    public String home(@RequestParam(name = "logout", required = false, defaultValue = "true") boolean logout, Model model) {
        System.out.println("==========> ACTIVATED");
        model.addAttribute("products", productEntityService.getMostPopularProduct());
        model.addAttribute("productsNewest", productEntityService.getNewestProduct());

        return "index";
    }





    @GetMapping("/login")
    String login() {
        return "app-user/login";
    }


//    @GetMapping("/")
//    public String getMostPopularProduct(Model model){
//
//
//
//
//
//        return "index";
//    }
}
