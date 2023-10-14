package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.GameType;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rust")
@RequiredArgsConstructor
public class RustController {

    private final ProductEntityService productEntityService;

    @GetMapping()
    public String getAllRustProduct(@PageableDefault(size = 20) Pageable pageable,
                                    Model model){
       model.addAttribute("allProducts", productEntityService.getAllProductByGameType(pageable));
//       model.addAttribute("productsSlider", productEntityService.getSliderByGameType(GameType.RUST));

        return "rustpage";
    }

}
