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
 
  @Query("SELECT c FROM Comment c WHERE c.itemId =:itemId ORDER BY c.thumbsUpNum DESC")
  List<Comment> findByItemIdOrderByThumbsUpNumDesc(@Param("itemId")Long itemId,Pageable pageable);

  @Query("SELECT c FROM Comment c WHERE c.userId =:userId ORDER BY c.thumbsUpNum DESC")
  List<Comment> findByUserId(@Param("userId")Long userId);

  @Query("SELECT COUNT(c)>0 FROM Comment c WHERE c.userId =:userId AND c.itemId =:itemId")
  boolean existsByUserIdAndItemId(@Param("userId")Long userId,@Param("itemId")Long itemId);

  @Query("SELECT COUNT(c)>0 FROM Comment c WHERE c.userId =:userId")
  boolean existsByUserId(@Param("userId")Long userId);

  @Query("SELECT COUNT(c)>0 FROM Comment c WHERE c.itemId =:itemId")
  boolean existsByItemId(@Param("itemId")Long itemId);

  @Query("SELECT AVG(c.score) FROM Comment c WHERE c.itemId = :itemId")
  Double calculateAverageScoreByItemId(@Param("itemId")Long itemId);
}
