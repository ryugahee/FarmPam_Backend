package com.fp.backend.account.dto;

import com.fp.backend.account.entity.Users;
import com.fp.backend.auction.entity.Item;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class UserInfoDto {

    private String realName;
    private String username;

    private String nickname;
    private String phoneNumber;
    private int age;
    private String email;
    private String mailCode;
    private String streetAddress;
    private String detailAddress;
    private Long farmMoney;

}
