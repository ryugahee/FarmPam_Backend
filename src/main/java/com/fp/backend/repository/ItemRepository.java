package com.fp.backend.repository;

import com.fp.backend.entity.Item;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ItemRepository extends JpaRepository<Item, Long> {

//    @Query
//    Page<Item> findByIsSoldoutFalseAndIdOrderByIdDesc(Long num, Pageable pageable);


    @Query("SELECT i FROM Item i WHERE i.isSoldout = false AND i.id > :num ORDER BY i.id DESC Limit 7")
    Slice<Item> findByIsSoldoutFalseAndIdOrderByIdDesc(@Param("num") Long num);

    @Query("SELECT i FROM Item i WHERE i.isSoldout = false AND i.id < :num ORDER BY i.id DESC Limit 7")
    Slice<Item> findByIsSoldoutFalseAndIdLessThanOrderByIdDesc(@Param("num") Long num);

}
