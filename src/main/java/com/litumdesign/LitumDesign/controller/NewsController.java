package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.*;
import com.litumdesign.LitumDesign.auth.AppUser;
import com.litumdesign.LitumDesign.service.NewsEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

        private final NewsEntityService newsEntityService;

    @GetMapping
    public String getAllNews(Model model, @PageableDefault(size = 1) Pageable pageable){
        model.addAttribute("allNews", newsEntityService.getAllNews(pageable));
        return "news";
    }


    @PostMapping("/addnews")
    @HxRequest
    public String addProductEntity(@RequestParam String title,
                                   @RequestParam String photoLink,
                                   @RequestParam String shortText,
                                   @RequestParam String text,
                                   @RequestParam String about,
                                   @AuthenticationPrincipal AppUser appUser,
                                   Model model, @PageableDefault(size = 1) Pageable pageable) throws GeneralSecurityException, IOException {




        NewsEntity newsEntity = new NewsEntity(
                photoLink,
                shortText,
                title,
                text,
                about,
                appUser.getName()
        );

        System.out.println("NEW NEWS ---------->> " + newsEntity);

        newsEntityService.addNewNews(newsEntity);


        try {
            model.addAttribute("allNews", newsEntityService.getAllNews(pageable));

        } catch (Exception e) {

            System.out.println("WE HAVE SOME PROBLEMS " + e.getMessage());
        }

        return "news"; // Имя вашего представленияa
    }

    @GetMapping("/getallnewshx")
    public String getAllNewsHx(Model model, @PageableDefault(size = 5) Pageable pageable){
        Page<NewsEntity> news = newsEntityService.getAllNews(pageable);

        System.out.println("pageble ->>>>" + news.getNumber());
        System.out.println("pageble ->>>>" + news.getTotalPages());

        if (news.getNumber() <= news.getTotalPages()-1) {

            model.addAttribute("allNews", news);
            model.addAttribute("link", "/news/getallnewshx?page=" + (news.getNumber() + 1));
//        model.addAttribute("link", "/getallnewshx?page=" + news.nextOrLastPageable());

            return "/fragments/newsfragment";
        }

        model.addAttribute("end", "end");
        return "/fragments/newsfragment";
    }


    @GetMapping("/getparticularnews")
    public String getAllNewsHx(Model model, @RequestParam("newsId") Long newsId){

        System.out.println(newsId);

        model.addAttribute("particularnews", newsEntityService.getParticularNews(newsId));

                return "/fragments/particularnewsfragment";
    }
}



