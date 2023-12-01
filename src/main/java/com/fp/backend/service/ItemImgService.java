package com.fp.backend.service;

import com.fp.backend.entity.ItemImg;
import com.fp.backend.repository.ItemImgRepository;
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

    private final FileService fileService;

    public String makeDir() {

        String folderPath = "C:\\auction\\item";

        File makeFolder = new File(folderPath);

        if(!makeFolder.exists()) {

            makeFolder.mkdirs();
            System.out.println("폴더를 생성합니다.");

        } else {
            System.out.println("이미 해당 폴더가 존재합니다.");
        }
        return folderPath;
    }

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        String itemImgLocation = makeDir();

        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/imges/item/" + imgName;
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

}
