package com.fp.backend.auction.repository;

import com.fp.backend.auction.entity.Item;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {


    //종료임박
   Slice<Item> findByIsSoldoutFalseOrderByTime(PageRequest pageable);
   //최신
    Slice<Item> findByIsSoldoutFalseOrderByIdDesc(PageRequest pageable);


    //스케줄러
    List<Item> findByTimeLessThan(long currentTimeMillis);

    //키워드 + 최신
    @Query("SELECT DISTINCT i FROM Item i " +
            "LEFT JOIN i.itemTagMapList itm " +
            "LEFT JOIN itm.itemTag it " +
            "WHERE (LOWER(i.itemTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(it.tagName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND i.isSoldout = false " +
            "ORDER BY i.id DESC")
    Slice<Item> findByKeywordAndLatest(@Param("keyword") String keyword, PageRequest pageable);

    //키워드 + 종료임박
    @Query("SELECT DISTINCT i FROM Item i " +
            "LEFT JOIN i.itemTagMapList itm " +
            "LEFT JOIN itm.itemTag it " +
            "WHERE (LOWER(i.itemTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(it.tagName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND i.isSoldout = false " +
            "ORDER BY i.time ASC")
    Slice<Item> findByKeywordAndTime(@Param("keyword") String keyword, PageRequest pageable);

    //경매 완료
//    Slice<Item> findByIsSoldoutTrueOrderByIdAsc(PageRequest pageable);
    @Query("SELECT i FROM Item i WHERE i.isSoldout = true ORDER BY i.id ASC")
    Slice<Item> findCompletedItemsOrderedByIdAsc(PageRequest pageable);

}



