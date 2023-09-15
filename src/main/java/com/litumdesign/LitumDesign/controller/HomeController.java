package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductEntityService productEntityService;

    @GetMapping
    public String home(@RequestParam(name = "logout", required = false, defaultValue = "true")  boolean logout, Model model, @PageableDefault(size = 5)  Pageable pageable ) {
        System.out.println("==========> ACTIVATED");

        Sort sort = pageable.getSort();

        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);

        }


        model.addAttribute("products", productEntityService.getMostPopularProduct());
        model.addAttribute("productsNewest", productEntityService.getNewestProduct());
        model.addAttribute("productsSlider", productEntityService.getSliderProduct());
        model.addAttribute("allProducts", productEntityService.getAllProductEntity(pageable));
        return "index";
    }


    @GetMapping("/products")
    @HxRequest
    private String getAllDoctors(Model model, @PageableDefault(size = 5)  Pageable pageable) {



        Sort sort = pageable.getSort();

        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);

        }

        model.addAttribute("allProducts", productEntityService.getAllProductEntity(pageable));
        return "fragments/allproductsfragment";
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
