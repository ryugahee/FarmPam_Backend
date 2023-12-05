package com.fp.backend.repository;

import com.fp.backend.entity.Item;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
//    ArrayList<Item> findAll();

    List<Item> findByIsSoldoutAndIdLessThanOrderByIdDesc(Boolean isSoldout, Long page, Pageable pageable);

//    Slice<Item> findByIsSoldoutAndIdOrderByIdDesc(Boolean isSoldout, Long cursorId, Pageable page);

//    Slice<Item> findByIsSoldoutAndIdLessThanOrderByIdDesc(Boolean isSoldout, Long cursorId, Pageable page);

//    Slice<Item> findByIsSoldout(Boolean isSoldout, Pageable page);

//    Slice<Item> findByIdLessThanAndIsSoldoutOrderByIdDesc(Long id, Boolean isSoldout, Pageable page);

}
