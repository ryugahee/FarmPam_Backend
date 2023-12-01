package com.fp.backend.repository;

import com.fp.backend.entity.Item;
import com.fp.backend.entity.ItemTagMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTagMapRepository extends JpaRepository<ItemTagMap, Long> {
    List<ItemTagMap> findAllByItem(Item item);
}
