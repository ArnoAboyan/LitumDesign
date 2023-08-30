package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.TestClass;
import com.litumdesign.LitumDesign.auth.AppUserController;
import com.litumdesign.LitumDesign.formaticUI.Toast;
import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

   private final ProductEntityRepository productEntityRepository;

    @GetMapping("/addfile")
    public String submitFilePage(@ModelAttribute TestClass testClass) {
        return "submitFilePage";

    }




    @PostMapping("/addfile")
    public String addFile(@Valid TestClass testClass, Errors errors, Model model, RedirectAttributes attributes) {

        if (errors.hasErrors()) {
            attributes.addFlashAttribute("toast", Toast.error("File not add!", "all bad"));
            // Если есть ошибки валидации, вернуть представление с ошибками
            System.out.println("---------> 1");
            return "submitfilepage"; // Имя вашего представления
        }

        try {

            attributes.addFlashAttribute("toast", Toast.success("Файл успешно добавлен", "all good"));
            System.out.println("---------> 2");
            return "redirect:/file/addfile";
        } catch (Exception e) {
            attributes.addFlashAttribute("toast", Toast.error("File not add!", e.getMessage()));
            System.out.println("---------> 3");
            return "redirect:/file/addfile";
        }

    }

}