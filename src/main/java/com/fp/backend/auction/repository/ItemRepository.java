package com.fp.backend.auction.repository;

import com.fp.backend.auction.entity.Item;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    Slice<Item> findByIsSoldoutFalseOrderByTime(PageRequest pageable);
    Slice<Item> findByIsSoldoutFalseOrderByIdDesc(PageRequest pageable);

    List<Item> findByTimeLessThan(long currentTimeMillis);

    @Query("SELECT DISTINCT i FROM Item i " +
            "LEFT JOIN i.itemTagMapList itm " +
            "LEFT JOIN itm.itemTag it " +
            "WHERE (LOWER(i.itemTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(it.tagName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND i.isSoldout = false")
    Slice<Item> findByKeywordAndNotSoldOut(@Param("keyword") String keyword, PageRequest pageable);

}

