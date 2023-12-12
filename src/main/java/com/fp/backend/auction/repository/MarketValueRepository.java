package com.fp.backend.auction.repository;

import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.entity.MarketValue;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MarketValueRepository extends JpaRepository<MarketValue, Long> {

    //날짜별 품목 시세
    @Query("SELECT AVG(i.averagePrice) AS average_price, DATE_FORMAT(i.soldDate, '%Y-%m-%d') AS day, i.itemType " +
            "FROM MarketValue i " +
            "WHERE i.itemType " +
            "LIKE %:itemType% " +
            "GROUP BY DATE_FORMAT(i.soldDate, '%Y-%m-%d'), i.itemType")
    List<MarketValue> findMarketValues(@Param("itemType") String itemType);



}
