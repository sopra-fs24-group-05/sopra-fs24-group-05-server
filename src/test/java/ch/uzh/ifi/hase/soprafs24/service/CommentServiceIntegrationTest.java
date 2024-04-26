package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Test class for commentResouce REST resource
 * Verify the behavior of the UserService by actually calling its methods and observing the state of the database
 * @see CommentService
 */

 @WebAppConfiguration
 @SpringBootTest
public class CommentServiceIntegrationTest {

  @Qualifier("commentRepository")
  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private CommentService commentService;

  @MockBean
  private ItemRepository itemRepository;

  private Comment testComment;

  @BeforeEach
  public void setup(){
    testComment = new Comment();
    testComment.setCommentId(1L);
    testComment.setUserId(1L);
    testComment.setItemId(1L);
    testComment.setScore(5L);
    testComment.setContent("test content");
    testComment.setThumbsUpNum(1L);
    commentRepository.deleteAll();
  }

  @Test
  public void createComment_validInput_success(){
    // before creating comment,there should be nothing in the database
    assertEquals(commentRepository.findById(1L),Optional.empty());

    // given

    Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);

    // when
    Comment createdComment = commentService.createComment(testComment);

    // then
    assertEquals(testComment.getCommentId(), createdComment.getCommentId());
    assertEquals(testComment.getUserId(), createdComment.getUserId());
    assertEquals(testComment.getItemId(), createdComment.getItemId());
    assertEquals(testComment.getScore(), createdComment.getScore());
    assertEquals(testComment.getContent(), createdComment.getContent());
    assertEquals(testComment.getThumbsUpNum(), createdComment.getThumbsUpNum());
  }

  @Test
  public void createComment_itemIdNotFound_throwsException(){
    assertEquals(commentRepository.findById(1L),Optional.empty());

    Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(false);

    assertThrows(ResponseStatusException.class, ()->commentService.createComment(testComment));
  }

  @Test
  public void createComment_userAlreadyComment_throwsException(){
    assertEquals(commentRepository.findById(1L),Optional.empty());

    Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);
    
    commentService.createComment(testComment);
    
    // if the user has already comment on this item, throws an exception
    assertThrows(ResponseStatusException.class, ()->commentService.createComment(testComment));
  }

  
  
}
