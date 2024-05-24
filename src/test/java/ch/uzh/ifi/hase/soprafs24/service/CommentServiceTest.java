package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.array;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
public class CommentServiceTest {
  
  @Mock
  private CommentRepository commentRepository;

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CommentService commentService;

  private Comment testComment;
  private Comment reply;

  private Optional<Comment> optionalTestComment;

  private List<Comment> testCommentList;

  private Item testItem;

  private User commentOwner;

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

    reply = new Comment();
    reply.setCommentId(2L);
    reply.setCommentOwnerId(2L);
    reply.setCommentOwnerName("reply owner");
    reply.setCommentItemId(1L);
    reply.setScore(null);
    reply.setContent("reply content");
    reply.setThumbsUpNum(1L);
    reply.setFatherCommentId(testComment.getCommentId());

    commentOwner = new User();
    commentOwner.setUserId(1L);
    commentOwner.setIdentity(UserIdentity.ADMIN); 


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
    Mockito.when(commentRepository.findById(testComment.getCommentId())).thenReturn(optionalTestComment);
    Mockito.when(commentRepository.findById(reply.getCommentId())).thenReturn(Optional.of(reply));
    Mockito.when(commentRepository.findByCommentOwnerId(Mockito.anyLong())).thenReturn(testCommentList);
    Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(commentOwner));
    //Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);
    Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testItem));
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
  public void createReply_validInput_replyCreated() {
    Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);
    Mockito.when(itemRepository.findByItemId(Mockito.any())).thenReturn(testItem);
    Mockito.when(commentRepository.findByFatherCommentId(Mockito.anyLong())).thenReturn(Collections.singletonList(reply));
    // given
    Long fatherCommentId = testComment.getCommentId();

    // when
    commentService.createReply(reply);

    // then
    List<Comment> foundReplies = commentRepository.findByFatherCommentId(fatherCommentId);
    assertEquals(1, foundReplies.size()); // Ensure only one reply is created
    Comment createdReply = foundReplies.get(0);
    assertEquals(reply.getCommentOwnerId(), createdReply.getCommentOwnerId());
    assertEquals(reply.getCommentOwnerName(), createdReply.getCommentOwnerName());
    assertEquals(reply.getCommentItemId(), createdReply.getCommentItemId());
    assertEquals(reply.getScore(), createdReply.getScore());
    assertEquals(reply.getContent(), createdReply.getContent());
    assertEquals(reply.getThumbsUpNum(), createdReply.getThumbsUpNum());
    assertEquals(testComment.getCommentId(),createdReply.getFatherCommentId()); // Ensure the fatherCommentId is not set for replies
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
  public void getReplyByFatherCommentId_validInput_success(){
    Mockito.when(commentRepository.findByFatherCommentId(Mockito.anyLong())).thenReturn(Collections.singletonList(reply));
    List<Comment> foundReplies = commentService.getReplyByFatherCommentId(testComment.getCommentId());

    Mockito.verify(commentRepository, times(1)).findByFatherCommentId(testComment.getCommentId());

    assertNotNull(foundReplies);
    assertEquals(1, foundReplies.size());
    assertEquals(reply.getCommentId(), foundReplies.get(0).getCommentId());
    assertEquals(reply.getCommentOwnerId(), foundReplies.get(0).getCommentOwnerId());
    assertEquals(reply.getContent(), foundReplies.get(0).getContent());
    assertEquals(reply.getFatherCommentId(), foundReplies.get(0).getFatherCommentId());
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

  @Test
  public void deleteCommentOrReply_AdminUser_Success() {
      // When admin user deletes a comment
      commentService.deleteCommentOrReply(commentOwner.getUserId(), testComment.getCommentId());

      // Verify that the comment was deleted and the item score was updated
      verify(commentRepository, times(1)).delete(testComment);
      verify(commentRepository, times(1)).flush();
      verify(itemRepository, times(1)).save(testItem);
      verify(itemRepository, times(1)).flush();
  }

  @Test
  public void deleteCommentOrReply_Reply_Success() {
      // When admin user deletes a reply
      commentService.deleteCommentOrReply(commentOwner.getUserId(), reply.getCommentId());
      //doNothing().when(commentRepository).delete(any());

      // Verify that the reply was deleted
      verify(commentRepository, times(1)).delete(reply);
      verify(commentRepository, times(1)).flush();
      verify(itemRepository, times(0)).save(any());  // Item should not be updated for replies
  }

}
