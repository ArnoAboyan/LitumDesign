package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.service.ProductEntityService;
import com.litumdesign.LitumDesign.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/vendor-page")
public class VendorPageController {

    private final ProductEntityService productEntityService;
    private final UserEntityService userEntityService;

    @GetMapping("/{vendorName}")
    public String openVendorPanel(
            @PathVariable("vendorName") String vendorName,
            Model model,
            @PageableDefault(size = 20) Pageable pageable,
            RedirectAttributes redirectAttributes) {

        try {
            Sort sort = pageable.getSort();
            List<Sort.Order> orders = sort.toList();

            for (Sort.Order order : orders) {
                String sortName = order.getProperty();
                model.addAttribute("sortName", sortName);
            }

            model.addAttribute("allProducts", productEntityService.findAllProductsByVendorName(vendorName, pageable));
            return "vendor-page";
        } catch (PropertyReferenceException e) {
            // Логирование ошибки, если это необходимо
            e.printStackTrace();

            // Добавление атрибута с сообщением об ошибке (может быть использовано в представлении)
            redirectAttributes.addFlashAttribute("error", "Error! Wrong request for sorting");

            // Перенаправление на страницу без сортировки
            return "redirect:/vendor-page/" + vendorName;
        }
    }
}



//        Sort sort = pageable.getSort();
//
//        List<Sort.Order> orders = sort.toList();
//        for (Sort.Order order : orders) {
//            String sortName = order.getProperty();
//            model.addAttribute("sortName", sortName);
//
//        }
//
//
//
//
//        model.addAttribute("allProducts", productEntityService.findAllProductsByVendorName(vendorName, pageable));
//
//
//
//            return "vendor-page";
//    }
//
//
//}
