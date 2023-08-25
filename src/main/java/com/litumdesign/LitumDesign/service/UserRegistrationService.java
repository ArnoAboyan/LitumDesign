//package com.litumdesign.LitumDesign.service;
//
//import com.litumdesign.LitumDesign.Entity.Role;
//import com.litumdesign.LitumDesign.Entity.Users;
//import com.litumdesign.LitumDesign.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.stereotype.Service;
//
//import java.nio.Buffer;
//
//@Service
//@RequiredArgsConstructor
//public class UserRegistrationService {
//
//    private final UserRepository userRepository;
//
//    public Boolean addNewUser(Users user){
//
//        userRepository.save(user);
//
//        return true;
//    }
//
//    public Boolean addNewUserByO2auth(OidcUser oidcUser){
//
//        Users user = userRepository.findDistinctBySub(oidcUser.getIdToken().getSubject());
//
//        if (user != null){
//
//            user.setEmail(oidcUser.getIdToken().getEmail());
//
//            userRepository.save(user);
//
//             System.out.println("User already exists ->" + oidcUser.getIdToken().getEmail());
//            System.out.println("Email has been updated! ->" + oidcUser.getIdToken().getEmail());
//         }
//
//         Users newUser = new Users();
//
//        newUser.setEmail(oidcUser.getEmail());
//        newUser.setSub(oidcUser.getSubject());
//
//        userRepository.save(newUser);
//
//         return true;
//    }
//
//}
