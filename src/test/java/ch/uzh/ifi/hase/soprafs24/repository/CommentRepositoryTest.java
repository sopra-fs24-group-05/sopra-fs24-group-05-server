package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@Import(TestDataSourceConfig.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // disable default H2 database
public class CommentRepositoryTest {
  
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CommentRepository commentRepository;

  private Comment comment;
  private Comment childComment;

  private List<Comment> backUpData;

  @BeforeEach
  public void setup(){
    backUpData = commentRepository.findAll();
    commentRepository.deleteAll();
    //entityManager.getEntityManager().createQuery("DELETE FROM Comment").executeUpdate();//清空数据

    comment = new Comment();
    comment.setCommentOwnerId(1L);
    comment.setCommentOwnerName("comment owner");
    comment.setCommentItemId(1L);
    comment.setScore(5L);
    comment.setContent("test content");
    comment.setThumbsUpNum(1L);

    commentRepository.saveAndFlush(comment);

    childComment = new Comment();
    childComment.setCommentOwnerId(1L);
    childComment.setCommentOwnerName("comment owner");
    childComment.setCommentItemId(1L);
    childComment.setScore(15L);
    childComment.setContent("test child content");
    childComment.setThumbsUpNum(1L);
    childComment.setFatherCommentId(comment.getCommentId());

    commentRepository.saveAndFlush(childComment);
  }

  @AfterEach
  public void recover(){
    commentRepository.deleteAll();
    commentRepository.saveAll(backUpData);

  }

  @Test
  public void findByUserId_success() {
    // given

    entityManager.persist(comment);
    entityManager.flush();

    // when
    List<Comment> found = commentRepository.findByCommentOwnerId(comment.getCommentOwnerId());

    // then
    assertEquals(2, found.size());
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
    Comment comment2 = new Comment();
    comment2.setCommentOwnerId(1L);
    comment2.setCommentOwnerName("comment owner");
    comment2.setCommentItemId(1L);
    comment2.setScore(15L);
    comment2.setContent("test content");
    comment2.setThumbsUpNum(1L);

    commentRepository.saveAndFlush(comment2);

    Double avgScore = commentRepository.calculateAverageScoreByCommentItemId(comment.getCommentItemId());

    assertEquals(avgScore, (comment.getScore()+comment2.getScore())/2);
  }

  @Test
  public void findByFatherCommentId_success() {
    commentRepository.saveAndFlush(comment);
    commentRepository.saveAndFlush(childComment);

    // when
    List<Comment> foundReplies = commentRepository.findByFatherCommentId(comment.getCommentId());

    // then
    assertEquals(1, foundReplies.size());
    Comment foundReply = foundReplies.get(0);
    assertNotNull(foundReply.getCommentId());
    assertEquals(childComment.getCommentId(), foundReply.getCommentId());
    assertEquals(childComment.getCommentOwnerId(), foundReply.getCommentOwnerId());
    assertEquals(childComment.getCommentItemId(), foundReply.getCommentItemId());
    assertEquals(childComment.getScore(), foundReply.getScore());
    assertEquals(childComment.getContent(), foundReply.getContent());
    assertEquals(childComment.getThumbsUpNum(), foundReply.getThumbsUpNum());
    assertEquals(childComment.getFatherCommentId(), foundReply.getFatherCommentId());
  }
}
