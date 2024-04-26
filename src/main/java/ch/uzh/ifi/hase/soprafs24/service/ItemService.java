package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {


    private final ItemRepository itemRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, TopicRepository topicRepository) {
        this.itemRepository = itemRepository;
        this.topicRepository = topicRepository;
    }

    public Item addItem(Item item) {
        item.setCreationDate(new Date()); // Set the current date as creation date
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

//    public Item updateItem(Long itemId, Item updatedItem) {
//        return itemRepository.findById(itemId)
//                .map(item -> {
//                    item.setName(updatedItem.getName());
//                    item.setDescription(updatedItem.getDescription());
//                    return itemRepository.save(item);
//                })
//                .orElseThrow(() -> new RuntimeException("Item not found with id " + itemId));
//    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    public void likeItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        item.addLike();
        itemRepository.save(item);
    }

    public void scoreItem(Long itemId, double score) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        item.addScore(score);
        itemRepository.save(item);
    }

    public List<Item> getItemsByTopicSortedByScore(Long topicId) {
        return itemRepository.findByTopicIdOrderByScoreDesc(topicId);
    }

    public List<Item> getItemsByTopicId(Long topicId) {
        return itemRepository.findByTopicId(topicId);
    }

    public List<Item> getItemsByTopicName(String topicName) {
        return itemRepository.findByTopicName(topicName);
    }

//    public Item addItemToTopic(Long topicId, Item item) {
//        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));
//        item.setTopic(topic);
//        return itemRepository.save(item);
//    }

    public Item addItemToTopic(Item item) {
        if(topicRepository.findByTopicId(item.getItemTopicId()) != null) {
            System.out.println("!!!");
            item.setTopic(topicRepository.findByTopicId(item.getItemTopicId()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }

        return itemRepository.save(item);
    }
}
