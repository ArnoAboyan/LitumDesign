package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.Role;
import com.litumdesign.LitumDesign.Entity.UserEntity;
import com.litumdesign.LitumDesign.auth.AppUser;
import com.litumdesign.LitumDesign.googledrive.GoogleDriveService;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserEntityService {

    private final UserRepository userRepository;
    private final GoogleDriveService googleDriveService;

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

    public UserEntity getUserById(String userId){

        return userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User not found...") );
    }


    public UserEntity getUserByName(String name){

        return userRepository.findByName(name);
    }

    @Transactional
    public UserEntity deleteUploadCounter(UserDetails userDetails){
       UserEntity userEntity = getUserById(userDetails.getUsername());

        userEntity.setCountOfUploads(userEntity.getCountOfUploads()-1);
        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity setNewRoleUserEntity(UserEntity userEntity, Role role){
        userEntity.setAuthorities(role);

        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity setNewNameUserEntity(UserEntity userEntity, String name){
        userEntity.setName(name);

        return userRepository.save(userEntity);
    }

    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity updateUserAccountDetails(UserDetails userDetails, String fullName, String displayName, String email) {

        UserEntity userEntity = userRepository.findById(userDetails.getUsername()).orElseThrow(() -> new NullPointerException("User with username " + userDetails.getUsername() + " not found!"));

        if (fullName != null) {
            userEntity.setFullName(fullName);
        }
        if (displayName != null) {
            userEntity.setName(displayName);
        }
        if (email != null) {
            userEntity.setEmail(email);
        }

        userRepository.save(userEntity);
    return userEntity;
    }

    public void setUserSocialLinks(String discord, String telegram, String twitter, String facebook, String linkedIn, String youTube, UserEntity userEntity) {

            userEntity.setLinkedIn_link(linkedIn);
            userEntity.setDiscord_link(discord);
            userEntity.setTelegram_link(telegram);
            userEntity.setTwitter_link(twitter);
            userEntity.setFacebook_link(facebook);
            userEntity.setYoutube_link(youTube);

        userRepository.save(userEntity);
    }

    public UserEntity uploadUserAvatar(UserEntity userEntity, MultipartFile avatar) {

        googleDriveService.uploadUserAvatar(userEntity, avatar);



        return userRepository.save(userEntity);
    }

    public UserEntity uploadUserBanner(UserEntity userEntity, MultipartFile banner) {

        googleDriveService.uploadUserBanner(userEntity, banner);



        return userRepository.save(userEntity);
    }
}
