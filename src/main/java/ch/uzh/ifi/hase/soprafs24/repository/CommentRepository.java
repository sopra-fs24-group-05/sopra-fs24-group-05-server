package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
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
  @Query("SELECT c FROM Comment c WHERE c.commentItemId = :commentItemId ORDER BY c.thumbsUpNum DESC")
  List<Comment> findByCommentItemIdOrderByThumbsUpNumDesc(@Param("commentItemId") Long commentItemId,Pageable pageable);

  @Query("SELECT c FROM Comment c WHERE c.commentOwnerId = :commentOwnerId ORDER BY c.thumbsUpNum DESC")
  List<Comment> findByCommentOwnerId(@Param("commentOwnerId") Long commentOwnerId);

  @Query("SELECT c FROM Comment c WHERE c.commentItemId = :commentItemId")
  List<Comment> findByCommentItemId(@Param("commentItemId") Long commentItemId);

  @Query("SELECT COUNT(c)>0 FROM Comment c WHERE c.commentOwnerId = :commentOwnerId AND c.commentItemId = :commentItemId")
  boolean existsByCommentOwnerIdAndCommentItemId(@Param("commentOwnerId") Long commentOwnerId, @Param("commentItemId") Long commentItemId);

  @Query("SELECT COUNT(c)>0 FROM Comment c WHERE c.commentOwnerId = :commentOwnerId")
  boolean existsByCommentOwnerId(@Param("commentOwnerId") Long commentOwnerId);

  @Query("SELECT COUNT(c)>0 FROM Comment c WHERE c.commentItemId = :commentItemId")
  boolean existsByCommentItemId(@Param("commentItemId") Long commentItemId);

  @Query("SELECT AVG(c.score) FROM Comment c WHERE c.commentItemId = :commentItemId")
  Double calculateAverageScoreByCommentItemId(@Param("commentItemId") Long commentItemId);
}
