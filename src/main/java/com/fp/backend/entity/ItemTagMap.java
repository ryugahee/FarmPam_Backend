package com.fp.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "item_tag_map")
public class ItemTagMap extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_tag_map_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_tag_id")
    private ItemTag itemTag;

    public ItemTagMap(Item item, ItemTag itemTag) {
        super();
        this.item = item;
        this.itemTag = itemTag;
    }
}
