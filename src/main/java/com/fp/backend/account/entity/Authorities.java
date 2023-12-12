package com.fp.backend.account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authorities {

//    @Id
//    @Column(name = "authority_name", length = 50)
//    private String authorityName;
//
//    private String username;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    private String username;

}

