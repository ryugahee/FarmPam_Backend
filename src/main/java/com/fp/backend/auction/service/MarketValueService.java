package com.fp.backend.auction.service;

import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.entity.MarketValue;
import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.auction.repository.ItemTagRepository;
import com.fp.backend.auction.repository.MarketValueRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketValueService {


    private final ItemRepository itemRepository;


    private final ItemTagRepository itemTagRepository;

    private final MarketValueRepository marketValueRepository;

    @Scheduled(cron = "0 0 6 * * *") // Scheduled to run every day at 6 AM
    public void insertSeasonScheduler() {
        // Get items that are sold out today
        List<Item> soldItems = itemRepository.findByIsSoldoutTrueAndTime(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.of("+09:00")));

        // Group sold items by tag name
        Map<String, List<Item>> itemsByTagName = soldItems.stream()
                .flatMap(item -> item.getItemTagMapList().stream()
                        .map(itemTagMap -> Map.entry(itemTagMap.getItemTag().getTagName(), item)))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        // Calculate average price for each tag name
        List<MarketValue> marketValues = itemsByTagName.entrySet().stream()
                .map(entry -> {
                    String tagName = entry.getKey();
                    List<Item> itemsWithTag = entry.getValue();
                    int averagePrice = calculateAveragePrice(itemsWithTag);
                    Timestamp soldDate = new Timestamp(itemsWithTag.get(0).getTime() * 1000); // Use the time of the first item
                    return new MarketValue(tagName, averagePrice, soldDate);
                })
                .collect(Collectors.toList());

        // Save market values to the database
        marketValueRepository.saveAll(marketValues);
    }

    private int calculateAveragePrice(List<Item> items) {
        int total = items.stream().mapToInt(Item::getLastBidPrice).sum();
        return total / items.size();
    }
}
