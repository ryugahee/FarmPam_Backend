//package com.fp.backend.system.config;
//
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//@Configuration
//@RequiredArgsConstructor
//public class DynamoConfig {
//    @Value("${aws.dynamodb.endpoint}")
//    private String endpoint;
//    @Value("${aws.region}")
//    private String region;
//    @Value("${aws.accessKey}")
//    private String accessKey;
//    @Value("${aws.secretKey}")
//    private String secretKey;
//
//    @Bean
//    public DynamoDBMapper dynamoDBMapper() {
//        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
//                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
//                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
//                .withTableNameOverride(null)
//                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
//                .build();
//
//        return new DynamoDBMapper(amazonDynamoDB(), mapperConfig);
//    }
//
//    @Primary
//    @Bean
//    public AWSCredentialsProvider awsCredentialsProvider() {
//        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey));
//    }
//
//    @Bean
//    public AmazonDynamoDB amazonDynamoDB() {
//        return AmazonDynamoDBClientBuilder
//                .standard()
//                .withEndpointConfiguration(
//                        new AwsClientBuilder.EndpointConfiguration(endpoint, region)
//                )
//                .withCredentials(awsCredentialsProvider())
//                .build();
//    }
//}
