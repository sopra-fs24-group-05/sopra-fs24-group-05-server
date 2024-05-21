package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository("chatRepository")
public interface ChatRepository extends JpaRepository<ChatMessage, Long>{

  @Query
  public List<ChatMessage> findByItemId(Long itemId);
  
} 