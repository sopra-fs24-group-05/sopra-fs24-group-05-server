package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


@Repository("topicRepository")
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findByTopicName(String TopicName);

  Topic findByTopicId(int id);

  Topic findByOwnerId(int ownerId);

  @Query("SELECT t FROM Topic t WHERE LOWER(t.topicName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Topic> searchByKeyword(@Param("keyword") String keyword);


    @Query(value = "SELECT t.*, SUM(i.likes + i.score_count) as popularity " +
            "FROM Topic t JOIN Item i ON t.id = i.topic_id " +
            "GROUP BY t.id " +
            "ORDER BY popularity DESC", nativeQuery = true)
    List<Topic> findMostPopularTopics();

    @Query("SELECT t FROM Topic t WHERE LOWER(SUBSTRING(t.topicName, 1, 1)) = LOWER(:prefix)")
    List<Topic> findByFirstLetter(@Param("prefix") String prefix);
}