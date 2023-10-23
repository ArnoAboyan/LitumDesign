package com.litumdesign.LitumDesign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductDetailsController {

    @GetMapping("/")
    public String submitFilePage() {
        return "productdetails";
    }

}
