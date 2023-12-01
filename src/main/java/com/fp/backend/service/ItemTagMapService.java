package com.fp.backend.service;

import com.fp.backend.entity.Item;
import com.fp.backend.entity.ItemTag;
import com.fp.backend.entity.ItemTagMap;
import com.fp.backend.repository.ItemTagMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemTagMapService {

    private final ItemTagService itemTagService;
    private final ItemTagMapRepository itemTagMapRepository;

    public void saveItemTag(Item item, List<String> tagNames) {

        if(tagNames.size() == 0) return;

        tagNames.stream()
                .map(itemTag ->
                        itemTagService.findByTagName(itemTag)
                                .orElseGet(() -> itemTagService.save(itemTag)))
                .forEach(itemTag -> mapItemTagToItem(item, itemTag));
    }


    private Long mapItemTagToItem(Item item, ItemTag itemTag) {

        return itemTagMapRepository.save(new ItemTagMap(item, itemTag)).getId();
    }

    public List<ItemTagMap> findItemTagListByItem(Item item) {

        return itemTagMapRepository.findAllByItem(item);
    }

}
