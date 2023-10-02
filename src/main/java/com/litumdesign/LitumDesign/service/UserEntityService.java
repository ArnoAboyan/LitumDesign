package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.UserEntity;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserEntityService {

    private final UserEntityService userEntityService;
    private final UserRepository userRepository;

    public void userDownloadCounter(UserEntity userEntitySession){

        userEntitySession.setCountOfDownloads(userEntitySession.getCountOfDownloads() + 1);

        userRepository.save(userEntitySession);
    }


}
