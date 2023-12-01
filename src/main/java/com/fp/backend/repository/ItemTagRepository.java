package com.fp.backend.repository;

import com.fp.backend.entity.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {

    Optional<ItemTag> findByTagName(String tagName);
}
