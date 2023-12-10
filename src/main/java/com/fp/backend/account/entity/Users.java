package com.fp.backend.account.entity;

import com.fp.backend.account.common.AuthorityName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Users implements UserDetails, OAuth2User {

    @Id
    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    private String realName;

    private String nickname;

    private boolean enabled;

    private Integer age;

    private String email;

    private String phoneNumber;

    private String imageUrl;

    private String mailCode;

    private String streetAddress;

    private String detailAddress;


//    private String refreshToken;


    //간편로그인
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    //일반 로그인
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthorityName.USER.getKey())); // ROLE_USER
        // 다른 권한을 추가할 수 있음

        return authorities;
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
    public String getName() {
        return this.username;
    }
}