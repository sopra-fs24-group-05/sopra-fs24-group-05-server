package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
  @Query("SELECT c FROM Comment c WHERE c.itemId =: itemId ORDER BY c.thumbsUpNum DESC")
  List<Comment> findByItemIdOrderByThumbsUpNumDesc(@Param("itemId")Long itemId,Pageable pageable);

  @Query("SELECT c FROM Comment c WHERE c.userId =: userId ORDER BY c.thumbsUpNum DESC")
  List<Comment> findByUserId(Long userId);

  @Query("SELECT COUNT(c)>0 FROM Comment c WHERE c.userId =: userId AND c.itemId =: itemId")
  boolean existsByUserIdAndItemId(Long userId,Long itemId);

    @Query("SELECT AVG(c.score) FROM Comment c WHERE c.itemId = :itemId")
    static Double calculateAverageScoreByItemId(Long itemId) {
        return null;
    }
}
