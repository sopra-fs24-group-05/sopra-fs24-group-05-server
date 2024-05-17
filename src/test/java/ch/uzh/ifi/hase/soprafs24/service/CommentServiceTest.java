package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.array;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CommentServiceTest {
  
  @Mock
  private CommentRepository commentRepository;

  @Mock
  private ItemRepository itemRepository;

  @InjectMocks
  private CommentService commentService;

  private Comment testComment;

  private Optional<Comment> optionalTestComment;

  private List<Comment> testCommentList;

  private Item testItem;

  @BeforeEach
  public void setup(){
    MockitoAnnotations.openMocks(this);

    // given
    testComment = new Comment();
    testComment.setCommentId(1L);
    testComment.setCommentOwnerId(1L);
    testComment.setCommentOwnerName("testComment owner");
    testComment.setCommentItemId(1L);
    testComment.setScore(5L);
    testComment.setContent("test content");
    testComment.setThumbsUpNum(1L);
    testComment.setLikedUserList(new ArrayList<Long>(Arrays.asList(1L, 2L, 3L)));


    testCommentList = Collections.singletonList(testComment);

    optionalTestComment = Optional.of(testComment);

    testItem = new Item();
    testItem.setItemId(1L);
    testItem.setLikes(0);
    testItem.setItemName("testItemName");
    testItem.setScore(0.0);
    testItem.setContent("test Item Description");

    Mockito.when(commentRepository.save(Mockito.any())).thenReturn(testComment);
    Mockito.when(commentRepository.existsByCommentOwnerIdAndCommentItemId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
    Mockito.when(commentRepository.calculateAverageScoreByCommentItemId(Mockito.any())).thenReturn(testComment.getScore().doubleValue());
    Mockito.when(commentRepository.existsByCommentItemId(Mockito.anyLong())).thenReturn(true);
    Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(optionalTestComment);
    Mockito.when(commentRepository.findByCommentOwnerId(Mockito.anyLong())).thenReturn(testCommentList);
    //Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);
  }

  @Test
  public void createComment_validInput_success(){
    Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);
    Mockito.when(itemRepository.findByItemId(Mockito.any())).thenReturn(testItem);

    Comment createdComment = commentService.createComment(testComment);

    Mockito.verify(commentRepository,Mockito.times(1)).save(Mockito.any());// commentRepository.save() has been called once

    assertEquals(testComment.getCommentId(), createdComment.getCommentId());
    assertEquals(testComment.getCommentOwnerId(), createdComment.getCommentOwnerId());
    assertEquals(testComment.getCommentItemId(), createdComment.getCommentItemId());
    assertEquals(testComment.getScore(), createdComment.getScore());
    assertEquals(testComment.getContent(), createdComment.getContent());
    assertEquals(testComment.getThumbsUpNum(), createdComment.getThumbsUpNum());
  }

  @Test
  public void calculateAverageScoreByItemId_validInput_success(){
    Double avgScore = commentService.calculateAverageScoreByItemId(testComment.getCommentItemId());

    Mockito.verify(commentRepository,Mockito.times(1)).existsByCommentItemId(Mockito.any());
    Mockito.verify(commentRepository,Mockito.times(1)).calculateAverageScoreByCommentItemId(Mockito.any());

    assertEquals(avgScore, testComment.getScore().doubleValue());
  }

  @Test
  public void getCommentByCommentId_validInput_success(){
    Mockito.when(commentRepository.existsById(Mockito.any())).thenReturn(true);
    Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(optionalTestComment);
    
    Comment getByCommentItComment = commentService.getCommentByCommentId(testComment.getCommentId());

    Mockito.verify(commentRepository,Mockito.times(1)).findById(Mockito.anyLong());

    assertEquals(testComment.getCommentId(), getByCommentItComment.getCommentId());
    assertEquals(testComment.getCommentOwnerId(), getByCommentItComment.getCommentOwnerId());
    assertEquals(testComment.getCommentItemId(), getByCommentItComment.getCommentItemId());
    assertEquals(testComment.getScore(), getByCommentItComment.getScore());
    assertEquals(testComment.getContent(), getByCommentItComment.getContent());
    assertEquals(testComment.getThumbsUpNum(), getByCommentItComment.getThumbsUpNum());
  }

  @Test
  public void getCommentByCommentId_invalidInput_throwsException(){
    Mockito.when(commentRepository.existsById(Mockito.any())).thenReturn(false);
    
    Mockito.verify(commentRepository,Mockito.times(0)).findById(Mockito.anyLong());

    assertThrows(ResponseStatusException.class, ()->commentService.getCommentByCommentId(1L));
  }

  @Test
  public void checkUserLiked_validInput_success() {
      Long commentId = testComment.getCommentId();

      // Mock the repository response
      Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment));

      // Case 1: UserId is in LikedUserList
      boolean result1 = commentService.checkUserLiked(1L, commentId);

      // Assert
      assertTrue(result1);

      // Case 2: UserId is not in LikedUserList
      boolean result2 = commentService.checkUserLiked(4L, commentId);

      // Assert
      assertFalse(result2);
  }

  @Test
  public void calculateThumbsUpNum_validInput_success() {
    // set requested commentId
    Long commentId = testComment.getCommentId();

    // mocked repository response has been set in setup()

    // Act
    int thumbsUpNum = commentService.calculateThumbsUpNum(commentId);

    // Assert
    assertEquals(3, thumbsUpNum); // Expecting 3 thumbs up
  }

  @Test
  public void addUserIdToLikedList_validInput_success() {
      // set a userId not in LikedUserList
      Long userId = 4L;
      // set requested commentId
      Long commentId = testComment.getCommentId();


      // Mock the repository response

      // Act
      commentService.addUserIdToLikedList(userId, commentId);

      // Assert
      assertTrue(testComment.getLikedUserList().contains(userId)); // userId has been correctly added to 
      Mockito.verify(commentRepository,Mockito.times(1)).save(Mockito.any());
      Mockito.verify(commentRepository,Mockito.times(1)).flush();

      // If we try to add userId(has been added to LikedUserList) again
      commentService.addUserIdToLikedList(userId, commentId);
      // times of calling save() & flush() will not increase
      Mockito.verify(commentRepository,Mockito.times(1)).save(Mockito.any());
      Mockito.verify(commentRepository,Mockito.times(1)).flush();
  }

}
