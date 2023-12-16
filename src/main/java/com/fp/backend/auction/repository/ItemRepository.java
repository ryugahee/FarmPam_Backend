package com.fp.backend.auction.repository;

import com.fp.backend.auction.entity.Item;
import com.fp.backend.user.dto.UserRankDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {


    //종료임박
   Slice<Item> findByIsSoldoutFalseOrderByTime(PageRequest pageable);
   //최신
    Slice<Item> findByIsSoldoutFalseOrderByIdDesc(PageRequest pageable);


    //경매상태 업데이트
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


    List<Item> findByIsSoldoutTrueAndTime(long localDateTime);


    //top3랭킹
    @Query("SELECT NEW com.fp.backend.user.dto.UserRankDto(u.username, u.nickname, u.imageUrl, COUNT(i.id)) " +
            "FROM Users u " +
            "JOIN u.items i " +
            "WHERE i.isSoldout = true " +
            "AND i.regTime BETWEEN :startOfMonth AND :endOfMonth " +
            "GROUP BY u.username, u.nickname, u.imageUrl " +
            "ORDER BY COUNT(i.id) DESC")
    List<UserRankDto> findItemCountDesc(@Param("startOfMonth") LocalDateTime startOfMonth,
                                        @Param("endOfMonth") LocalDateTime endOfMonth,
                                        Pageable pageable);
}



