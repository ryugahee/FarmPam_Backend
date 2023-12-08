package com.fp.backend.auction.service;

import com.fp.backend.controller.FileS3UploadController;
import com.fp.backend.entity.ItemImg;
import com.fp.backend.repository.ItemImgRepository;
import com.fp.backend.auction.entity.ItemImg;
import com.fp.backend.auction.repository.ItemImgRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    private final ItemImgRepository itemImgRepository;

    private final FileS3UploadController fileS3UploadController;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(oriImgName)) {
            imgUrl = fileS3UploadController.s3FileUpload(itemImgFile);
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
}
