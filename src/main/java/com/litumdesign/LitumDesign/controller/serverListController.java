package com.litumdesign.LitumDesign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/serverlist")
public class serverListController {
    @GetMapping
    public String getServerList(){
        return "serverlist";
    }

}
