package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


@Repository("topicRepository")
public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {
  Topic findByTopicName(String TopicName);

  Topic findByTopicId(Long id);

  Topic findByOwnerId(Long ownerId);

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

/*
 * Some methods provided by JpaRepository and can be used to manipulate date in datebase
 * 
 * save(EntityClass entity): Saves the given entity. If the entity has not been persisted before, 
 * it will be saved as a new entity. If the entity has been persisted before, it will be updated.
 * 
 * saveAndFlush(EntityClass entity): save and entityManager.flush(sent sql need to be conducted to the database)
 * 
 * Delete:
   deleteById(ID id): Deletes the entity with the given ID.
   delete(T entity): Deletes the given entity.
   deleteAll(): Deletes all entities managed by the repository.
   
   Find:
   findById(ID id): Retrieves an entity by its ID.
   findAll(): Retrieves all entities managed by the repository.
   findAllById(Iterable<ID> ids): Retrieves entities by their IDs.
   count(): Returns the number of entities managed by the repository.

   Existence check:
   existsById(ID id): Checks if an entity with the given ID exists.

   Custom queries:
   findByName(String name)
   findByXXXAndYYY use the "And" keyword, similarly there are "Or"
   findByZZZisNull
 */