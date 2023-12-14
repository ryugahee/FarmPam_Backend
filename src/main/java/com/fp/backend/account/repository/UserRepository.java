package com.fp.backend.account.repository;

import com.fp.backend.account.entity.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

    Optional<Users> findByUsernameAndEmail(String username, String email); // 계정 중복 회원가입 아닌지

//    Optional<Users> findByUsernameAndPassword(String username, String password);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);


    @EntityGraph(attributePaths = "authorities") // Lazy조회가 아닌 Eager 조회로 authorities 정보를 같이 가져오게 됨
    Optional<Users> findOneWithAuthoritiesByUsername(String username);



}
