//package com.litumdesign.LitumDesign.controller;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.apache.http.HttpStatus;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class MyErrorController implements ErrorController{
//
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if(statusCode == HttpStatus.SC_NOT_FOUND) {
//                return "errors/error-404";
//            }
//            else if(statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
//                return "errors/error-500";
//            }
//            else if(statusCode == HttpStatus.SC_FORBIDDEN) {
//                return "errors/error-403";
//            }
//            else if(statusCode == HttpStatus.SC_BAD_REQUEST) {
//                return "errors/error-400";
//            }
//        }
//        return "errors/error";
//    }
//
//}
//
