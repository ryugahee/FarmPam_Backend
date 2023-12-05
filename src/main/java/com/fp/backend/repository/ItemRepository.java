package com.fp.backend.repository;

import com.fp.backend.entity.Item;
import com.google.gson.stream.JsonToken;
import net.minidev.json.JSONUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
//    ArrayList<Item> findAll();

//    List<Item> findByIsSoldout(Boolean isSoldout);

 /*   Slice<Item> findByIdLessThanOrderByIdDesc(Boolean isSoldout, int lastId, PageRequest pageRequest);
    Slice<Item> findByIsSoldoutAndIdLessThanOrderByIdDesc(Boolean isSoldout, int lastId, PageRequest pageRequest);
*/

    Slice<Item> findByIsSoldoutAndIdOrderByIdDesc(Boolean isSoldout, Long cursorId, Pageable page);

    Slice<Item> findByIsSoldoutAndIdLessThanOrderByIdDesc(Boolean isSoldout, Long cursorId, Pageable page);


}
