package com.litumdesign.LitumDesign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/vendor-page")
public class VendorPageController {

    @GetMapping()
    public String openVendorPanel(Model model, @AuthenticationPrincipal UserDetails userDetails ){


            return "vendor-page";
    }


}
