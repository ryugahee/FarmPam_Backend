package com.fp.backend.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileS3UploadController {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping
    public String s3FileUpload(MultipartFile multipartFile) throws IOException {
        UUID uuid = UUID.randomUUID();
        String originalFilename = multipartFile.getOriginalFilename();

        String changeFileName = "img/" + uuid + "_" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());


        amazonS3Client.putObject(bucket, changeFileName, multipartFile.getInputStream(), metadata);

        String s3Url = amazonS3Client.getUrl(bucket, changeFileName).toString();

        return s3Url;
    }
}