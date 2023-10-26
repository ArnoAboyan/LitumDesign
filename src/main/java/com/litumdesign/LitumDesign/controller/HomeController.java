package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.UserEntity;
import com.litumdesign.LitumDesign.auth.AppUser;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLOutput;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {
    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;

    @GetMapping
    public String home(@RequestParam(name = "logout", required = false, defaultValue = "true")  boolean logout, Model model, @PageableDefault(size = 20)  Pageable pageable) {
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


    @PostMapping
    @HxRequest
    public String getAllProducts(Model model, @PageableDefault(size = 20)  Pageable pageable) {



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
    public String login() {
        return "app-user/login";
    }



    @PostMapping("/cookiechecker")
    @HxRequest
     public String cookiesChecker(@AuthenticationPrincipal AppUser appUser, HttpSession session)  {
        session.setAttribute("cookiecheck", true);

        System.out.println("COOKIE  APP USER -> " + appUser);
        userEntityService.cookieChecker(appUser);
    return "fragments/cookiefragment";
    }

    @PostMapping("/cookiecheckenoauthorizations")
    @HxRequest
    public String cookiesChecker(HttpSession session)  {
        session.setAttribute("cookiecheck", true);
        return "fragments/cookiefragment";
    }

}
