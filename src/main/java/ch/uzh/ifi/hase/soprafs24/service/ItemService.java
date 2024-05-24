package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemService {
    
    private final ItemRepository itemRepository;
    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;

    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    @Autowired
    public ItemService(ItemRepository itemRepository, TopicRepository topicRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.topicRepository = topicRepository;
        this.commentRepository = commentRepository;
    }

    public Item addItem(Item newItem) {
        
        newItem.setCreationDate(new Date()); // Set the current date as creation date

        checkIfItemExists(newItem);

        newItem = itemRepository.save(newItem); // save item
        topicRepository.flush();

        log.debug("Created Information for Item: {}", newItem);

        return newItem;
    }

    private void checkIfItemExists(Item itemToBeCreated) {
        Item itemByItemName = itemRepository.findByItemName(itemToBeCreated.getItemName());
        String baseErrorMessage = "The %s provided %s not unique. Therefore, the item could not be created!";
        if (itemByItemName != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "add Item failed because itemName already exists");
        }
    }

    public Item updateItem(Item itemInput) {
        Item item = itemRepository.findById(Long.valueOf(itemInput.getItemId())).get();
        if(item == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item with ItemId was not found");
        }
        return itemRepository.save(itemInput);
    }


    public void likeItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        item.addLike();
        itemRepository.save(item);
    }

    public List<Item> getItemsByTopicSortedByScore(Integer topicId) {
        return itemRepository.findByTopicIdOrderByScoreDesc((topicId));
    }

    public List<Item> getItemsByTopicId(Integer topicId) {
        return itemRepository.findByTopicId(topicId);
    }

    public List<Item> getItemsByTopicName(String topicName) {return itemRepository.findByTopicName(topicName);}

    public Item getItemByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        item.incrementPopularity();
        itemRepository.save(item);
        return item;
    }

    public Item addItemToTopic(Integer topicId, Item item) {
        Topic topic = topicRepository.findById(Long.valueOf(topicId)).orElseThrow(() -> new RuntimeException("Topic not found"));
        item.setTopic(topic);
        return itemRepository.save(item);
    }

    public Double getItemAverageScore(Long itemId) {
        return commentRepository.calculateAverageScoreByCommentItemId(itemId);
    }

    public void deleteItemByItemName(String itemName) {
        Item item = itemRepository.findByItemName(itemName);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        else {
            itemRepository.delete(item);
        }
    }

    public void deleteItemByItemId(Long itemId) {
        if (!itemRepository.existsById(Long.valueOf(itemId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        itemRepository.deleteById(Long.valueOf(itemId));
    }



    public List<Item> searchItemsByKeyword(String keyword) {
        return itemRepository.findByKeyword(keyword);
    }

    public List<Item> getItemsSortedByCommentCountAndTopicId(Integer topicId) {
        // 获取所有属于该 topicId 的 Item
        List<Item> items = itemRepository.findByTopicId(topicId);

        // 获取所有评论
        List<Comment> comments = commentRepository.findAll();

        // 统计每个 Item 的评论数量
        Map<Long, Long> itemCommentCountMap = comments.stream()
                .filter(comment -> items.stream()
                        .anyMatch(item -> item.getItemId().equals(comment.getCommentItemId())))
                .collect(Collectors.groupingBy(Comment::getCommentItemId, Collectors.counting()));

        // 将没有评论的 Item 评论数视为0
        items.forEach(item -> itemCommentCountMap.putIfAbsent(item.getItemId(), 0L));

        // 按评论数量排序
        List<Long> sortedItemIds = itemCommentCountMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 按照排序后的 ID 列表顺序重新排列查询结果
        Map<Long, Item> itemMap = items.stream()
                .collect(Collectors.toMap(Item::getItemId, item -> item));

        // 返回按评论数量排序的 Item 列表
        return sortedItemIds.stream()
                .map(itemMap::get)
                .collect(Collectors.toList());
    }




    public List<Item> getItemsSortedByPopularity() {
        return itemRepository.findAllByOrderByPopularityDesc(); // 需要在 ItemRepository 中添加对应的方法
    }
}
