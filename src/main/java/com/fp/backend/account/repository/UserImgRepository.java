package com.fp.backend.account.repository;

import com.fp.backend.account.entity.UserImg;
import com.fp.backend.account.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImgRepository extends JpaRepository<UserImg, Long> {

   Optional<UserImg> findByUser(Optional<Users> user);

}
