package com.fp.backend.account.repository;

import com.fp.backend.account.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Authorities a SET a.authority = :authority WHERE a.username = :username")
    void updateAuthorityByUsername(@Param("username") String username, @Param("authority") String authority);

    List<Authorities> findByUsername(String username);

}
