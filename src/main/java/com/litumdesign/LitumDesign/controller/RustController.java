package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Categories;
import com.litumdesign.LitumDesign.Entity.GameType;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rust")
@RequiredArgsConstructor
public class RustController {

    private final ProductEntityService productEntityService;

    @GetMapping()
    public String getAllRustProduct(@PageableDefault(size = 20) Pageable pageable,
                                    Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }


        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndSort(GameType.RUST, pageable));
        model.addAttribute("gamename", "rust");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.RUST));
        return "rustpage";
    }
    @PostMapping
    @HxRequest
    public String getAllHXRustProduct(@PageableDefault(size = 20) Pageable pageable,
                                    Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }


        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndSort(GameType.RUST, pageable));
        model.addAttribute("gamename", "rust");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.RUST));
        return "fragments/allproductsfragment";
    }

    @GetMapping("/{category}")
    public String getRustPluginByCategory(@PageableDefault(size = 20) Pageable pageable,
                                @PathVariable Categories category,
                                Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }


        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndCategoriesAndSort(GameType.RUST, category,pageable));
        model.addAttribute("category", category);
        model.addAttribute("gamename", "rust");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.RUST));
        return "rustpage";
    }

    @PostMapping("/{category}")
    @HxRequest
    public String getHXRustPluginByCategory(@PageableDefault(size = 20) Pageable pageable,
                                          @PathVariable Categories category,
                                          Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }


        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndCategoriesAndSort(GameType.RUST, category,pageable));
        model.addAttribute("category", category);
        model.addAttribute("gamename", "rust");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.RUST));
        return "fragments/allproductsfragment";
    }

}
