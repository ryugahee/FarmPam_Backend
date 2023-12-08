package com.fp.backend.auction.repository;

import com.fp.backend.auction.entity.Item;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ItemRepository extends JpaRepository<Item, Long> {

    // 경매 리스트 최신순 조회
    @Query("SELECT i FROM Item i WHERE i.isSoldout = false AND i.id > :num ORDER BY i.id DESC Limit 10")
    Slice<Item> findByIsSoldoutFalseAndIdOrderByIdDesc(@Param("num") Long num);

    @Query("SELECT i FROM Item i WHERE i.isSoldout = false AND i.id < :num ORDER BY i.id DESC Limit 10")
    Slice<Item> findByIsSoldoutFalseAndIdLessThanOrderByIdDesc(@Param("num") Long num);

}
