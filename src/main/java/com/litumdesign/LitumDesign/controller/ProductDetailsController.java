package com.litumdesign.LitumDesign.controller;

import com.litumdesign.LitumDesign.Entity.Access;
import com.litumdesign.LitumDesign.Entity.CommentProductEntity;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.repository.CommentProductRepository;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductDetailsController {

    private final ProductEntityService productEntityService;
    private final CommentProductRepository commentProductRepository;

    @GetMapping("/details/{productId}/{productName}")
    public String findProductById(@PathVariable Long productId,
                                  Model model, @AuthenticationPrincipal UserDetails userDetails) {

       ProductEntity productEntity =  productEntityService.findProductEntityById(productId);

       if(productEntity.getAccess().equals(Access.PUBLIC)){
           model.addAttribute("productdetails", productEntity);

           model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));

           productEntityService.viewsCounter(productEntity);
           return "productdetails";
       } else if (userDetails.getUsername() !=null && userDetails.getUsername().equals(productEntity.getUploadUserId().getLogin())) {
           model.addAttribute("productdetails", productEntity);

           model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));

           productEntityService.viewsCounter(productEntity);
           return "productdetails";
       }else return "errors/error-404";
    }


    @GetMapping("/comment/reply")
    @HxRequest
    public String openReplyTextarea(){



        return "fragments/comment-reply-fragment";
    }



    @PostMapping("/comment/add-new-comment")
    @HxRequest
    public String addReview(@RequestParam ("review") String review,
                                  @RequestParam ("productid") Long productId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model)
    {

        if (userDetails == null){
            model.addAttribute("error", "Error while adding review!");
            return "fragments/product-review-fragment";
        }
        ProductEntity productEntity =  productEntityService.findProductEntityById(productId);
            if (!productEntityService.addNewReview(review, productId, userDetails)){
                model.addAttribute("productdetails", productEntity);
                model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));
                model.addAttribute("error", "Error while adding review!");
                return "fragments/product-review-fragment";
            }
        model.addAttribute("productdetails", productEntity);
        model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));
        model.addAttribute("message", "Review has been added successfully!");
        return "fragments/product-review-fragment";
    }

    @PostMapping("/comment/add-child-comment")
    @HxRequest
    public String addChildReview(@RequestParam ("review") String review,
                            @RequestParam ("productid") Long productId,
                            @RequestParam ("parentId") Long parentId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model)
    {
        ProductEntity productEntity =  productEntityService.findProductEntityById(productId);

        if (userDetails == null){
            model.addAttribute("error", "Error while adding comment!");
            model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));
            return "fragments/product-review-fragment";
        }

        if (!productEntityService.addNewChildReview(review, productId, parentId, userDetails)){
            model.addAttribute("productdetails", productEntity);
            model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));
            model.addAttribute("error", "Error while adding comment!");
            return "fragments/product-review-fragment";
        }
        model.addAttribute("productdetails", productEntity);
        model.addAttribute("message", "Comment has been added successfully!");
        model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));
        return "fragments/product-review-fragment";
    }


    @DeleteMapping("/comment/delete-comment")
    @HxRequest
    public String deleteReview(@RequestParam ("reviewId") Long reviewId,
                               @RequestParam ("productId") Long productId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model){


        ProductEntity productEntity = productEntityService.findProductEntityById(productId);

        if (userDetails == null){
            model.addAttribute("productdetails", productEntity);
            model.addAttribute("error", "Error while removing review!");
            model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));
            return "fragments/product-review-fragment";
        }

        CommentProductEntity commentProduct = commentProductRepository.
                findById(reviewId).orElseThrow(() -> new NullPointerException("Comment not found!"));

        if (commentProduct.getUserEntity().getLogin().equals(userDetails.getUsername())){
        productEntityService.deleteReview(reviewId);
        model.addAttribute("productdetails", productEntity);
        model.addAttribute("message", "Review has been deleted successfully!");
        model.addAttribute("comments", productEntityService.findAllByProductEntityIdAndParentIsNull(productEntity.getId()));
        return "fragments/product-review-fragment";
        }
        return "errors/error-500";
    }
}
