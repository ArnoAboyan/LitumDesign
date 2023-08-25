package com.litumdesign.LitumDesign.auth;

import com.litumdesign.LitumDesign.Entity.*;
//import com.litumdesign.LitumDesign.repository.AuthorityEntityRepository;
import com.litumdesign.LitumDesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Value
@RequiredArgsConstructor
@NonFinal
public class AppUserService implements UserDetailsManager {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    //    AuthorityEntityRepository authorityEntityRepository;
    Executor executor;

    //TODO change "username" in DB maybe on "login" and make id not EMAIL(maybe SUBJECT )
    //TODO write APP create/login service.


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(" ->> LOAD USER BY DB");

        return userRepository
                .findById(username)
                .map(ue -> AppUser
                        .builder()
                        .username(ue.getUsername())
                        .password(ue.getPassword())
                        .name(ue.getName())
                        .sub(ue.getSub())
                        .locale(ue.getLocale())
                        .rank(ue.getRank().name())
                        .fullName(ue.getFullName())
                        .discordTag(ue.getDiscordTag())
                        .telegramTag(ue.getTelegramTag())
                        .email(ue.getEmail())
                        .countOfDownloads(ue.getCountOfDownloads())
                        .countOfUploads(ue.getCountOfUploads())
                        .imageUrl(ue.getImageUrl())
                        .provider(ue.getProvider())
                        .createdAt(ue.getCreatedAt())
                        .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + ue.getAuthorities().name()))

//                                ue
//                                .getUserAuthorities()
//                                .stream()
//                                .map(a -> new SimpleGrantedAuthority(a.getAuthority().getName()))
//                                .toList()
                        )
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found!", username)));

    }


    OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler() {
        return userRequest -> {
            LoginProvider provider = getProvider(userRequest);
            OidcUserService delegate = new OidcUserService();
            OidcUser oidcUser = delegate.loadUser(userRequest);
            AppUser appUser = null;

            if (userExists(oidcUser.getEmail())) {
                System.out.println("GETTING ======> " + oidcUser);
                appUser = (AppUser) loadUserByUsername(oidcUser.getEmail());
                System.out.println("LADING USER -> " + appUser);
                return appUser;
            } else
                appUser = AppUser
                        .builder()
                        .provider(provider)
                        .username(oidcUser.getEmail())
                        .name(oidcUser.getName())
                        .locale(oidcUser.getLocale())
                        .email(oidcUser.getEmail())
                        .fullName(oidcUser.getFullName())
                        .sub(oidcUser.getSubject())
                        .imageUrl(oidcUser.getAttribute("picture"))
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .attributes(oidcUser.getAttributes())
                        .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)))
                        .build();

            saveOauth2User(appUser);

            System.out.println("NEW o2auth USER " + appUser);

            return appUser;
        };
    }


    private void saveOauth2User(AppUser appUser) {
        CompletableFuture.runAsync(() -> createUser(appUser), executor);
    }

    private LoginProvider getProvider(OAuth2UserRequest userRequest) {
        return LoginProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
    }


    private void createUser(AppUser user) {
        UserEntity userEntity = saveUserIfNotExists(user);

//        List<AuthorityEntity> authorities = user
//                .authorities
//                .stream()
//                .map(a -> saveAuthorityIfNotExists(a.getAuthority(), user.getProvider()))
//                .toList();
//
//        userEntity.mergeAuthorities(authorities);
//
//        userRepository.save(userEntity);

        System.out.println("STEP -2 ");


    }

//    private AuthorityEntity saveAuthorityIfNotExists(String authority, LoginProvider provider) {
//        return authorityEntityRepository
//                .findByName(authority)
//                .orElseGet(() -> authorityEntityRepository
//                        .save(
//                                new AuthorityEntity(authority, provider)
//                        ));
//    }


    private UserEntity saveUserIfNotExists(AppUser user) {
        return userRepository
                .findById(user.getUsername())
                .orElseGet(() -> userRepository
                        .save(new UserEntity(
                                user.getUsername(),
                                user.getSub(),
                                user.getPassword(),
                                user.getEmail(),
                                user.getName(),
                                user.getImageUrl(),
                                user.getProvider(),
                                user.getCreatedAt(),
                                Rank.NEWBIE,
                                user.getFullName(),
                                Role.USER,
                                user.getDiscordTag(),
                                user.getTelegramTag(),
                                user.getCountOfDownloads(),
                                user.getCountOfUploads(),
                                user.getLocale()
                        )));
    }


    public void createUser(String username, String password) {


        createUser(User
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)))
                .build());
    }


    @Override
    public void createUser(UserDetails user) {

        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException(String.format("User %s already exists!", user.getUsername()));
        }

        createUser(AppUser
                .builder()
                .provider(LoginProvider.APP)
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build());
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {


    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        System.out.println("USER EXIST =>>" + userRepository.existsById(username));
        return userRepository.existsById(username);
    }


}
