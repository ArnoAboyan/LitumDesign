package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Categories;
import com.litumdesign.LitumDesign.Entity.GameType;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/unturned")
@RequiredArgsConstructor
public class UnturnedController {

    private final ProductEntityService productEntityService;

    @GetMapping()
    public String getAllUnturnedProduct(@PageableDefault(size = 20) Pageable pageable,
                                    Model model){

        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndSort(GameType.UNTURNED, pageable));
        model.addAttribute("gamename", "unturned");

        return "unturnedpage";
    }

    @GetMapping("/{category}")
    public String getUnturnedPluginByCategory(@PageableDefault(size = 20) Pageable pageable,
                                @PathVariable Categories category,
                                Model model){

        System.out.println("SORT1 ->" + pageable.getSort());

        model.addAttribute("allProducts", productEntityService.getAllProductByGameTypeAndCategoriesAndSort(GameType.UNTURNED, category, pageable));
        model.addAttribute("category", category);
        model.addAttribute("gamename", "unturned");

        return "unturnedpage";
    }

}
