package com.fp.backend.service;

import com.fp.backend.entity.Auction;
import com.fp.backend.repository.AuctionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepo auctionRepo;
    public Auction saveAuction(Auction auction){
        return auctionRepo.saveAuction(auction);
    }
    public Auction getAuctionById(String auctionId){
        return auctionRepo.getAuctionById(auctionId);
    }

    public String updateAuction(Auction auction){
        return auctionRepo.updateAuction(auction);
    }
}
