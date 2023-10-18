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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/unturned")
@RequiredArgsConstructor
public class UnturnedController {

    private final ProductEntityService productEntityService;

    @GetMapping()
    public String getAllUnturnedProduct(@PageableDefault(size = 20) Pageable pageable,
                                    Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }


        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndSort(GameType.UNTURNED, pageable));
        model.addAttribute("gamename", "unturned");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.UNTURNED));

        return "unturnedpage";
    }

    @PostMapping
    public String getHXAllUnturnedProduct(@PageableDefault(size = 20) Pageable pageable,
                                        Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }


        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndSort(GameType.UNTURNED, pageable));
        model.addAttribute("gamename", "unturned");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.UNTURNED));

        return "fragments/allproductsfragment";
    }

    @GetMapping("/{category}")
    public String getUnturnedPluginByCategory(@PageableDefault(size = 20) Pageable pageable,
                                @PathVariable Categories category,
                                Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }

        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndCategoriesAndSort(GameType.UNTURNED, category, pageable));
        model.addAttribute("category", category);
        model.addAttribute("gamename", "unturned");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.UNTURNED));

        return "unturnedpage";
    }

    @PostMapping("/{category}")
    @HxRequest
    public String getHXUnturnedPluginByCategory(@PageableDefault(size = 20) Pageable pageable,
                                              @PathVariable Categories category,
                                              Model model){

        Sort sort = pageable.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            String sortName = order.getProperty();
            model.addAttribute("sortName", sortName);
        }

        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndCategoriesAndSort(GameType.UNTURNED, category, pageable));
        model.addAttribute("category", category);
        model.addAttribute("gamename", "unturned");
        model.addAttribute("products", productEntityService.getMostPopularProductWithGameType(GameType.UNTURNED));

        return "fragments/allproductsfragment";
    }

}
