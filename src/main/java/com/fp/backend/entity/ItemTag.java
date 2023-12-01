package com.fp.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "item_tag")
public class ItemTag extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_tag_id")
    private Long id;

    @Column(nullable = false)
    private String tagName;

    @OneToMany(mappedBy = "itemTag")
    private List<ItemTagMap> itemTagMapList = new ArrayList<>();

    @Builder
    public ItemTag(String tagName) {
        this.tagName = tagName;
    }
}
