package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("topicRepository")
public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    Topic findByTopicName(String TopicName);

    Topic findByTopicId(Integer id);

    List<Topic> findByOwnerId(Integer ownerId);

    @Query("SELECT t FROM Topic t WHERE t.topicName LIKE %:keyword%")
    List<Topic> searchByKeyword(String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE Topic t SET t.searchCount = t.searchCount + 1 WHERE t.topicId IN :ids")
    void incrementSearchCount(List<Integer> ids);

    @Query("SELECT t FROM Topic t ORDER BY t.searchCount DESC")
    List<Topic> findMostPopularTopics();

    @Query("SELECT t FROM Topic t WHERE LOWER(SUBSTRING(t.topicName, 1, 1)) = LOWER(:prefix)")
    List<Topic> findByFirstLetter(@Param("prefix") String prefix);

    //boolean existsById(Integer topicId);
}
