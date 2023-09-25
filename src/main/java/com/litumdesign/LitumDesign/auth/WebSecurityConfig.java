package com.litumdesign.LitumDesign.auth;

//import com.litumdesign.LitumDesign.service.UserRegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;
;

@Configuration
@EnableWebSecurity
@Log4j2
public class WebSecurityConfig {
    @Autowired
//private UserRegistrationService userRegistrationService;


    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception{
        return http
                .securityMatcher("/image/**", "/**.css", "/**.js" )
                .authorizeHttpRequests(c -> c.anyRequest().permitAll())
                .securityContext(c->c.disable())
                .sessionManagement(c->c.disable())
                .requestCache(c->c.disable())
                .build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AppUserService appUserService) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//                        .requestMatchers("/admin-user-page").hasAnyRole(ADMIN, USER)
//                        .requestMatchers("/admin-page").hasRole(ADMIN)
//                        .requestMatchers("/user-page").hasRole(USER)
//                        .requestMatchers("/", "/login","/auth", "/public-page").permitAll()
                        .requestMatchers("/", "/products", "/login","/logout", "/error", "/news/getallnewshx", "/news/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())

                .formLogin(c->c.loginPage("/login").loginProcessingUrl("/authenticate")
                        .usernameParameter("user")
                        .passwordParameter("pass")
                        .defaultSuccessUrl("/")
                )
               .oauth2Login(oc -> oc.loginPage("/login")
                       .userInfoEndpoint(ui -> ui.oidcUserService(appUserService.oidcLoginHandler()))
                       .defaultSuccessUrl("/", true ))
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
@Bean
   public   PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();

    }



    @Bean
    ApplicationListener<AuthenticationSuccessEvent> successLogin(){
        return event -> {

            log.info("success: {}", event.getAuthentication());

        };

    }


}
