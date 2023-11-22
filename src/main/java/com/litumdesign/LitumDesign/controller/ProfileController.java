package com.litumdesign.LitumDesign.controller;


import com.litumdesign.LitumDesign.service.UserEntityService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
@Log4j2
public class ProfileController {
    private final UserEntityService userEntityService;

    @GetMapping("/account-details")
    public String accountDetails(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            userEntityService.getUserById(userDetails.getUsername());

            model.addAttribute("user", userEntityService.getUserById(userDetails.getUsername()));

            return "userprofilepage";
        }
        return "errors/error-404";
    }

    @PostMapping("/update-account-details")
    public String updateAccountDetails(@RequestParam String fullName,
                                       @RequestParam String displayName,
                                       @RequestParam @Email(message = "Invalid email address") String email,
                                       Model model,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        try {
            userEntityService.updateUserAccountDetails(userDetails, fullName, displayName, email);
            model.addAttribute("message", "Changes applied successfully!");
            model.addAttribute("user", userEntityService.getUserById(userDetails.getUsername()));
        } catch (Exception e){
            log.error("Error while update account details " + e);
            model.addAttribute("error", "Error! No changes applied");

        }
            return "fragments/profileFragments/accountDetailsUpdateForm";
    }
}

