//package com.litumdesign.LitumDesign.service;
//
//import com.litumdesign.LitumDesign.Entity.ProductEntity;
//import com.litumdesign.LitumDesign.repository.ProductEntityRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Log4j2
//public class DetailsProductService {
//
//    private final ProductEntityRepository productEntityRepository;
//
//
//    public ProductEntity findProductDetailsEntityById(Long productId){
//
//       return productEntityRepository.findById(productId).orElseThrow(()-> new NullPointerException("Product not find"));
//
//    }
//
//
//}
