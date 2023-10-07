package com.litumdesign.LitumDesign.auth;

import com.litumdesign.LitumDesign.Entity.LoginProvider;
import com.litumdesign.LitumDesign.Entity.UserShopEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
public class AppUser implements UserDetails, OidcUser {
    String login;
    String password;

    String sub;
    String email;
    String name;
    String imageUrl;
    Map<String, Object> attributes;

    LocalDateTime createdAt;

    String rank;

    String fullName;
    Boolean cookie;
    String discordTag;
    String telegramTag;
    int countOfDownloads;
    int countOfUploads;
    String locale;
    Boolean punishment;
    Boolean verified;
    Collection<? extends GrantedAuthority> authorities;

    LoginProvider provider;

    List<UserShopEntity> userShop;

    @Override
    public String getName() {
        return Objects.nonNull(name) ? name : login;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
