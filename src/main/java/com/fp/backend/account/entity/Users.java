package com.fp.backend.account.entity;

import com.fp.backend.account.common.AuthorityName;
import com.fp.backend.auction.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Entity
public class Users implements UserDetails, OAuth2User {

    @Id
    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 1000000, nullable = true)
    private String password;

    private boolean enabled;

    private String realName;

    private String nickname;

    private String phoneNumber;

    private boolean phoneCheck;

    private Integer age;

    private String email;

    private String mailCode;

    private String streetAddress;

    private String detailAddress;

    private String imageUrl;

    private Long farmMoney;

    @Override
    public <A> A getAttribute(String name) {
        if (name.equals("GUEST")) {
            return OAuth2User.super.getAttribute("ROLE_GUEST");
        } else {
            return OAuth2User.super.getAttribute("ROLE_USER");
        }

    }

    //간편로그인
    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();

        // 권한을 추가하고 싶은 경우
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthorityName.GUEST.getKey())); // ROLE_GUEST
        // 다른 권한을 추가할 수 있음

        // Map에 "authorities" 키에 권한 정보를 추가
        attributes.put("authorities", authorities);

        // 다른 속성들을 추가할 수 있음

        return attributes;
    }

    //일반 로그인
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthorityName.GUEST.getKey())); // ROLE_USER
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

    public void updateFarmMoney(Long farmMoney) {
        this.farmMoney += farmMoney;
    }

    public Long payFarmMoney(Long amount) {
        this.farmMoney -= amount;
        return farmMoney;
    }
}