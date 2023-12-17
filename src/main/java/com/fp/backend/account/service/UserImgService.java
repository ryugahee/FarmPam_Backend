package com.fp.backend.account.service;

import com.fp.backend.account.dto.UserInfoDto;
import com.fp.backend.account.entity.UserImg;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserImgRepository;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.controller.FileS3UploadController;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserImgService {
    private final UserImgRepository userImgRepository;

    private final FileS3UploadController fileS3UploadController;

    private final UserRepository userRepository;

    public void saveUserImg(UserInfoDto dto, MultipartFile userImgFile, HttpServletRequest request) throws Exception {
        String oriImgName = userImgFile.getOriginalFilename();

        String imgName = "";
        String imgUrl = "";

        String usernameBefore = request.getHeader("username");

        byte[] decodedBytes = Base64.getDecoder().decode(usernameBefore);
        String username = new String(decodedBytes, StandardCharsets.UTF_8);

        Optional<Users> user = userRepository.findByUsername(username);

      Users user2 = user.orElse(null);

        System.out.println(user.get());


        if (!StringUtils.isEmpty(oriImgName)) {
            imgUrl = fileS3UploadController.s3FileUpload(userImgFile);
        }

        Users updatedUser = Users.builder()
                .username(dto.getUsername())
                .password(user2.getPassword())
                .realName(dto.getRealName())
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .age(dto.getAge())
                .email(dto.getEmail())
                .mailCode(dto.getMailCode())
                .streetAddress(dto.getStreetAddress())
                .detailAddress(dto.getDetailAddress())
                .imageUrl(imgUrl)
                .farmMoney(dto.getFarmMoney())
                .build();

        userRepository.save(updatedUser);

        Optional<UserImg> img = userImgRepository.findByUser(user);

        System.out.println("기존 이미지가 있는지 : " + img.isPresent());

        UserImg userImg;

        if (!img.isPresent()) {
             userImg = UserImg.builder()
                    .oriImgName(oriImgName)
                    .imgName(imgName)
                    .user(user2)
                    .imgUrl(imgUrl)
                    .build();

        } else {

             userImg = UserImg.builder()
                    .id(img.get().getId())
                    .oriImgName(oriImgName)
                    .imgName(imgName)
                    .user(user2)
                    .imgUrl(imgUrl)
                    .build();
        }

        userImgRepository.save(userImg);
    }
}
