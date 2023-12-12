package com.fp.backend.account.repository;

import com.fp.backend.account.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

    Optional<AccessToken> findByAccessToken(String accessToken);

    Optional<AccessToken> findByUsername(String username);

    //     사용자명(username)으로 AccessToken을 찾아서 AccessToken 값을 수정하는 메소드
    @Modifying
    @Transactional
    @Query("UPDATE AccessToken a SET a.accessToken = :newAccessToken WHERE a.username = :username")
    int updateAccessTokenByUsername(@Param("username") String username, @Param("newAccessToken") String newAccessToken);

    Optional<AccessToken> findAccessTokenByAccessToken(String accessToken);

}
