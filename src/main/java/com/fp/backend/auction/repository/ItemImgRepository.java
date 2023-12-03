package com.fp.backend.auction.repository;

import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItem(Item item);

    List<ItemImg> findByItemAndRepImgYn(Item item, String repImgYn);

}
