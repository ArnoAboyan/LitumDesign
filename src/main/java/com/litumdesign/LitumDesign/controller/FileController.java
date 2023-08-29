package com.litumdesign.LitumDesign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileController {

    @GetMapping("/submitafile")
    public String submitFilePage (){
        return "submitFilePage";

    }


    @GetMapping("/addfile")
    public String addFile(Model model){


        return "redirect:/";
    }

}
