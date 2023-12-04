//package com.fp.backend.system.config;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.google.api.client.util.Value;
//import org.springframework.context.annotation.Bean;
//
//public class S3Config {
//    @Value("${cloud.aws.s3.region}")
//    private String region;
//
//    @Value("${cloud.aws.credentials.accessKey}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secretKey}")
//    private String secretKey;
//
//    // 외부 의존성을 Bean으로 등록해서 DI를 통해 주입할 수 있도록 함
//    @Bean
//    public AmazonS3 amazonS3Client() {
//        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//
//        return AmazonS3ClientBuilder
//                .standard()
//                .withCredentials(new AWSStaticCredentials  Provider(credentials))
//                .withRegion(region)
//                .build();
//    }
//}