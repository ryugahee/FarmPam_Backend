package com.fp.backend.repository;

import com.fp.backend.entity.Item;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>{
//    ArrayList<Item> findAll();

    List<Item> findByIsSoldout(Boolean isSoldout);
//List<Item> findByIsSoldoutOrderByIdDesc(Boolean isSoldout, int lastId, PageRequest pageRequest);

}
