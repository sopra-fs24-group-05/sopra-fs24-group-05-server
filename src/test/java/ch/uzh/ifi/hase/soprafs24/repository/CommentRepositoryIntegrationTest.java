package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CommentRepositoryIntegrationTest {
  
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CommentRepository commentRepository;

  private Comment comment;

  @BeforeEach
  public void setup(){
    comment = new Comment();
    comment.setCommentOwnerId(1L);
    comment.setCommentOwnerName("comment owner");
    comment.setCommentItemId(1L);
    comment.setScore(5L);
    comment.setContent("test content");
    comment.setThumbsUpNum(1L);
  }

  @Test
  public void findByUserId_success() {
    // given

    entityManager.persist(comment);
    entityManager.flush();

    // when
    List<Comment> found = commentRepository.findByCommentOwnerId(comment.getCommentOwnerId());

    // then
    assertEquals(found.size(), 1);
    assertNotNull(found.get(0).getCommentId());
    assertEquals(found.get(0).getCommentItemId(), comment.getCommentItemId());
    assertEquals(found.get(0).getCommentOwnerId(), comment.getCommentOwnerId());
    assertEquals(found.get(0).getScore(), comment.getScore());
    assertEquals(found.get(0).getContent(), comment.getContent());
    assertEquals(found.get(0).getThumbsUpNum(), comment.getThumbsUpNum());
  }

  @Test
  public void existsByUserId_success() {
    // given

    entityManager.persist(comment);
    entityManager.flush();

    boolean exists = commentRepository.existsByCommentOwnerId(comment.getCommentOwnerId());

    assertTrue(exists);
  }

  @Test
  public void existsByItemId_success() {
    // given

    entityManager.persist(comment);
    entityManager.flush();

    boolean exists = commentRepository.existsByCommentItemId(comment.getCommentItemId());

    assertTrue(exists);
  }

  @Test
  public void existsByUserIdAndItemId_success() {
    // given

    entityManager.persist(comment);
    entityManager.flush();

    boolean exists = commentRepository.existsByCommentOwnerIdAndCommentItemId(comment.getCommentOwnerId(),comment.getCommentItemId());

    assertTrue(exists);
  }

  @Test
  public void calculateAverageScoreByItemId_success(){
    // given
    Comment comment1 = new Comment();
    comment1.setCommentOwnerId(1L);
    comment1.setCommentOwnerName("comment1 owner");
    comment1.setCommentItemId(1L);
    comment1.setScore(5L);
    comment1.setContent("test content");
    comment1.setThumbsUpNum(1L);

    Comment comment2 = new Comment();
    comment2.setCommentOwnerId(1L);
    comment2.setCommentOwnerName("comment2 owner");
    comment2.setCommentItemId(1L);
    comment2.setScore(15L);
    comment2.setContent("test content");
    comment2.setThumbsUpNum(1L);

    entityManager.persist(comment1);
    entityManager.persist(comment2);
    entityManager.flush();

    Double avgScore = commentRepository.calculateAverageScoreByCommentItemId(comment1.getCommentItemId());

    assertEquals(avgScore, (comment1.getScore()+comment2.getScore())/2);
  }

}
