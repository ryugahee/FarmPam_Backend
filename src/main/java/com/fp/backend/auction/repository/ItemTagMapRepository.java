package com.fp.backend.auction.repository;

import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.entity.ItemTagMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTagMapRepository extends JpaRepository<ItemTagMap, Long> {
    List<ItemTagMap> findAllByItem(Item item);

    void deleteByItem(Item target);
}
