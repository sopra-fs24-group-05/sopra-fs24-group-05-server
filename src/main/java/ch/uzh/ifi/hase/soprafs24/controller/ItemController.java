package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/items/byTopicId/{topicId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ItemGetDTO addItem(@RequestBody ItemPostDTO itemPostDTO) {

        Item itemInput = new Item();

        BeanUtils.copyProperties(itemPostDTO, itemInput);

        Item addedItem = itemService.addItem(itemInput);

        return DTOMapper.INSTANCE.convertEntityToItemGetDTO(addedItem);
    }
    
    @PostMapping("/items/{itemId}/like")
    public ResponseEntity<?> likeItem(@PathVariable Long itemId) {
        itemService.likeItem(itemId);
        return ResponseEntity.ok().build();
    }

    
    @GetMapping("/items/sortedByScore/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<ItemGetDTO>> getItemsByTopicSortedByScore(@PathVariable Integer topicId) {
        List<Item> items = itemService.getItemsByTopicSortedByScore(topicId);
        List<ItemGetDTO> itemGetDTOs = items.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemGetDTOs);
    }

  //YZQ_DEV_M3
    @GetMapping("/items/getByItemId/{itemId}")
    public ResponseEntity<Item> getItemByItemId(@PathVariable Long itemId) {
        Item item = itemService.getItemByItemId(itemId);
        return ResponseEntity.ok(item);
    }

  //main
  
    @GetMapping("/items/byTopicId/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<ItemGetDTO>> getItemsByTopicId(@PathVariable Integer topicId) {
        List<Item> items = itemService.getItemsByTopicId(topicId);
        List<ItemGetDTO> itemGetDTOs = items.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemGetDTOs);
    }

    @GetMapping("/items/byTopicName/{topicName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<ItemGetDTO>> getItemsByTopicName(@PathVariable String topicName) {
        List<Item> items = itemService.getItemsByTopicName(topicName);
        List<ItemGetDTO> itemGetDTOs = items.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemGetDTOs);
    }

    @GetMapping("/{itemId}/score")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Double> getItemAverageScore(@PathVariable Long itemId) {
        Double averageScore = itemService.getItemAverageScore(itemId);
        return ResponseEntity.ok(averageScore);
    }

    @DeleteMapping("/items/{topicId}/{itemName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemByItemName(@PathVariable Integer topicId, @PathVariable String itemName) {
        itemService.deleteItemByItemName(itemName);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopicById(@PathVariable Long itemId) {
        itemService.deleteItemByItemId(itemId);
    }

    @GetMapping("/items/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ItemGetDTO>> searchItemsByKeyword(@RequestParam String keyword) {
        List<Item> items = itemService.searchItemsByKeyword(keyword);
        List<ItemGetDTO> itemGetDTOs = items.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemGetDTOs);
    }

    @GetMapping("/items/sortedByCommentCount/{topicId}")
    public ResponseEntity<List<ItemGetDTO>> getItemsSortedByCommentCountAndTopicId(@PathVariable Integer topicId) {
        List<Item> items = itemService.getItemsSortedByCommentCountAndTopicId(topicId);
        List<ItemGetDTO> itemGetDTOs = items.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemGetDTOs);
    }

    @GetMapping("/items/sortedByPopularity")
    public ResponseEntity<List<ItemGetDTO>> getItemsSortedByPopularity() {
        List<Item> items = itemService.getItemsSortedByPopularity();
        List<ItemGetDTO> itemGetDTOs = items.stream()
                .map(DTOMapper.INSTANCE::convertEntityToItemGetDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemGetDTOs);
    }
}