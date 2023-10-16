package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Categories;
import com.litumdesign.LitumDesign.Entity.GameType;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/rust")
@RequiredArgsConstructor
public class RustController {

    private final ProductEntityService productEntityService;

    @GetMapping()
    public String getAllRustProduct(@PageableDefault(size = 20) Pageable pageable,
                                    Model model){
        System.out.println("SORT1 ->" + pageable.getSort());

        Sort sort = pageable.getSort();
        String sortName = "UNSORTED";
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            sortName = order.getProperty();
            System.out.println("SORT NAME -> " + sortName);
        }

        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeRustAndSort(pageable));




        return "rustpage";
    }

    @GetMapping("/{category}")
    public String getRustPluginByCategory(@PageableDefault(size = 20) Pageable pageable,
                                @PathVariable Categories category,
                                Model model){

        System.out.println("SORT1 ->" + pageable.getSort());

        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndCategoriesAndSort(category,pageable));
        model.addAttribute("category", category);

        return "rustpage";
    }

//    @GetMapping("/{category}")
//    public String getRustPluginByCategoryAndSort(@PageableDefault(size = 20) Pageable pageable,
//                                @PathVariable Categories category,
//                                                 Model model){
//
//        System.out.println("SORT2 ->" + pageable.getSort());
//
//        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndCategories(category,pageable));
//        model.addAttribute("category", category);
//
//        return "rustpage";
//    }
}
