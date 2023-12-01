//package com.fp.backend.repository;
//
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
//import com.fp.backend.entity.Auction;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class AuctionRepo {
//
//    private final DynamoDBMapper dynamoDBMapper;
//    //생성
//    public Auction saveAuction(Auction auction){
//        dynamoDBMapper.save(auction);
//        return auction;
//    }
//    //조회
//    public Auction getAuctionById(String auctionId){
//        return dynamoDBMapper.load(Auction.class, auctionId);
//    }
//    //수정
//    public String updateAuction(Auction auction){
//        dynamoDBMapper.save(auction, new DynamoDBSaveExpression()
//                .withExpectedEntry("auctionId", new ExpectedAttributeValue(
//                        new AttributeValue().withS(auction.getId())
//                )));
//        return auction.getId();
//    }
//}
