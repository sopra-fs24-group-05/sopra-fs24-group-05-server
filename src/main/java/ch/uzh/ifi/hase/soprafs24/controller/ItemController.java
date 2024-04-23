package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/{itemId}/like")
    public ResponseEntity<?> likeItem(@PathVariable Long itemId) {
        itemService.likeItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{itemId}/score")
    public ResponseEntity<?> scoreItem(@PathVariable Long itemId, @RequestBody double rating) {
        itemService.scoreItem(itemId, rating);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sorted-by-score")
    public ResponseEntity<List<Item>> getItemsSortedByScore(@PathVariable Long topicId) {
        List<Item> items = itemService.getItemsByTopicSortedByScore(topicId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/by-topic-id")
    public ResponseEntity<List<Item>> getItemsByTopicId(@RequestParam Long topicId) {
        List<Item> items = itemService.getItemsByTopicId(topicId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/by-topic-name")
    public ResponseEntity<List<Item>> getItemsByTopicName(@RequestParam String topicName) {
        List<Item> items = itemService.getItemsByTopicName(topicName);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Item> addItemToTopic(@PathVariable Long topicId, @RequestBody Item item) {
        Item newItem = itemService.addItemToTopic(topicId, item);
        return ResponseEntity.ok(newItem);
    }

}
