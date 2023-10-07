package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.UserEntity;
import com.litumdesign.LitumDesign.auth.AppUser;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserEntityService {

    private final UserRepository userRepository;

    public void userDownloadCounter(UserDetails userDetailsSession){


        userDetailsSession.getUsername();
        UserEntity userEntity = userRepository.findDistinctByEmail(userDetailsSession.getUsername());
        userEntity.setCountOfDownloads(userEntity.getCountOfDownloads()+1);
        userRepository.save(userEntity);
    }

    public void cookieChecker(AppUser appUser){



        UserEntity userEntity= userRepository.findById(appUser.getLogin()).orElseThrow(() -> new NullPointerException("User for cookies not found"));
        userEntity.setCookie(true);
        userRepository.save(userEntity);

    }

}
