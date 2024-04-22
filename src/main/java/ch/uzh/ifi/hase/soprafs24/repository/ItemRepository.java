package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.topic.id = :topicId ORDER BY i.totalScore / i.scoreCount DESC")
    List<Item> findByTopicIdOrderByScoreDesc(@Param("topicId") Long topicId);

    @Query("SELECT i FROM Item i WHERE i.topic.id = :topicId")
    List<Item> findByTopicId(@Param("topicId") Long topicId);

    @Query("SELECT i FROM Item i WHERE i.topic.topicName = :topicName")
    List<Item> findByTopicName(@Param("topicName") String topicName);


}