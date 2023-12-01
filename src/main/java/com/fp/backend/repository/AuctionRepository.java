package com.fp.backend.repository;

import com.fp.backend.entity.Auction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuctionRepository extends CrudRepository<Auction, String> {
    List<Auction> findByUserName(String userName);
    List<Auction> findByBidPrice(String bidPrice);
}
