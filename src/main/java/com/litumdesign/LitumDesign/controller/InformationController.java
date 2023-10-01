package com.litumdesign.LitumDesign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/information")
@RequiredArgsConstructor
public class InformationController {

    @GetMapping("/privacypolicy")
    public String showPrivacyPolicy(){

        return "privacypolicy";
    }

}
