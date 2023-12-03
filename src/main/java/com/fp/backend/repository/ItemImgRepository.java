package com.fp.backend.repository;

import com.fp.backend.entity.Item;
import com.fp.backend.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemAndRepImgYn(Item item, String repImgYn);

}
