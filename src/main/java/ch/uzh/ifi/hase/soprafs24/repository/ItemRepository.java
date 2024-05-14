package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("itemRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByItemName(String itemName);

    Item findByItemId(Long itemId);

    @Query("SELECT i FROM Item i WHERE i.itemId = :itemId")
    Item findByCustomQuery(@Param("itemId") Long itemId);

    @Query("SELECT i FROM Item i WHERE i.topic.topicId = :topicId ORDER BY i.score DESC")
    List<Item> findByTopicIdOrderByScoreDesc(@Param("topicId") Integer topicId);

    @Query("SELECT i FROM Item i WHERE i.topic.topicId = :topicId")
    List<Item> findByTopicId(@Param("topicId") Integer topicId);

    @Query("SELECT i FROM Item i WHERE i.topic.topicName = :topicName")
    List<Item> findByTopicName(@Param("topicName") String topicName);
}