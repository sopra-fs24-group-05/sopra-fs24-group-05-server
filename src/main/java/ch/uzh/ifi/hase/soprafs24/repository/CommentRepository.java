package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("commentRepository")
public interface CommentRepository extends JpaRepository<Comment, Long>{



  /**
   * Return first n comments ordered by thumbsUpNum in descending order
   * Pegeable is used to specify page number of the return result, 
   * number of elements per page, and sorting method
   * @param pageable
   * @return
   */
  @Query("SELECT c FROM Comment c ORDER BY c.thumbsUpNum DESC")
  List<Comment> findByOrderByThumbsUpNumDesc(Pageable pageable);

  
}
