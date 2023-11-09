package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.Role;
import com.litumdesign.LitumDesign.Entity.UserEntity;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPanelController {

    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;


    @GetMapping("/adminpanel")
    public String openAdminPanel(Model model, @AuthenticationPrincipal UserDetails userDetails ){

        if (userDetails.getUsername() != null){

            List<ProductEntity> productEntityList = productEntityService.findAll();
            ;


            model.addAttribute("allvendorproducts", productEntityList);
            model.addAttribute("sumofdownloads", productEntityService.sumOfDownloads(productEntityList));
            model.addAttribute("sumofviews", productEntityService.sumOfViews(productEntityList));

            return "adminpanel";
        } else return "errors/error-404";
    }

    @GetMapping("/users")
    public String usersPanel(Model model, @AuthenticationPrincipal UserDetails userDetails ){

        if (userDetails.getUsername() != null) {

            List<UserEntity> userEntityList = userEntityService.findAllUsers();


            model.addAttribute("allusers", userEntityList);

            return "admin-panel-users";
        }else return "errors/error-404";
    }

    @PostMapping("/update-role")
    @HxRequest
    public String changeUserRole(@RequestParam("role") Role role,
                                 @RequestParam("login") String login,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {



            UserEntity userEntity = userEntityService.getUserById(login);

            userEntityService.setNewRoleUserEntity(userEntity, role);

            model.addAttribute("user", userEntity);

            return "fragments/adminPaneleFragments/admin-panel-user-role-fragment";
    }

    @PostMapping("/update-name")
    @HxRequest
    public String changeUserName(@RequestParam("name") String name,
                                 @RequestParam("login") String login,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {



        UserEntity userEntity = userEntityService.getUserById(login);

        userEntityService.setNewNameUserEntity(userEntity, name);

        model.addAttribute("user", userEntity);

        return "fragments/adminPaneleFragments/admin-panel-user-name-fragment";
    }

//    @GetMapping("/adminpanel/bycategory/{category}")
//    @HxRequest
//    public String getProductByCategoryHx(@PathVariable Categories category, Model model, @AuthenticationPrincipal UserDetails userDetails ){
//
//
//        List<ProductEntity> productEntityList =  productEntityService.findByVendorIdAndCategories(userEntityService.getUserById(userDetails.getUsername()), category);
//        model.addAttribute("vendorproductsbycategory", productEntityList);
//
//        return "fragments/vendorpanelproductsfragment";
//    }

//    @GetMapping("adminpanel/allproducts")
//    @HxRequest
//    public String getAllProductHx(Model model, @AuthenticationPrincipal UserDetails userDetails ){
//
//        List<ProductEntity> productEntityList = productEntityService.findAllByVendorId(userEntityService.getUserById(userDetails.getUsername()));
//        model.addAttribute("vendorproductsbycategory", productEntityList);
//
//        return "fragments/vendorpanelproductsfragment";
//    }

//    @GetMapping("adminpanel/search")
//    @HxRequest
//    public String findProductHx(@RequestParam String searchquery, Model model, @AuthenticationPrincipal UserDetails userDetails ){
//
//        List<ProductEntity> productEntityList = productEntityService.getSearchResultForVendors(searchquery, userEntityService.getUserById(userDetails.getUsername()));
//        model.addAttribute("vendorproductsbycategory", productEntityList);
//
//        return "fragments/admin-panel-users-fragment";
//    }

}

