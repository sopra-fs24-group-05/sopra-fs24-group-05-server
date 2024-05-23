package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.config.WebSocketConfig;
import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * Test class for commentResouce REST resource
 * Verify the behavior of the UserService by actually calling its methods and observing the state of the database
 * @see CommentService
 */

@WebAppConfiguration
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CommentServiceIntegrationTest {

  @Qualifier("commentRepository")
  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private CommentService commentService;

  @MockBean
  private ItemRepository itemRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private WebSocketConfig webSocketConfig;

  private Comment testComment;
  private List<Comment> backUpData;
  private Item testItem;
  private User commentOwner;

  @BeforeEach
  public void setup(){

    backUpData = commentRepository.findAll();
    commentRepository.deleteAll();
    testComment = new Comment();
    //testComment.setCommentId(1L); // calling the actual repository, no need to set this field
    testComment.setCommentOwnerId(1L);
    testComment.setCommentOwnerName("testComment owner");
    testComment.setCommentItemId(1L);
    testComment.setScore(5L);
    testComment.setContent("test content");
    testComment.setThumbsUpNum(1L);

    testItem = new Item();
    testItem.setItemId(1L);
    testItem.setLikes(0);
    testItem.setItemName("testItemName");
    testItem.setScore(0.0);
    testItem.setContent("test Item Description");

    commentOwner = new User();
    commentOwner.setUserId(testComment.getCommentOwnerId());
    commentOwner.setAvatar("this is a test avatar");

    Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(commentOwner));
    
    commentRepository.deleteAll();
  }

  @AfterEach
  public void recover(){
    commentRepository.deleteAll();
    commentRepository.saveAll(backUpData);
  }

  @Test
  public void createComment_validInput_success(){
    // before creating comment,there should be nothing in the database
    assertEquals(commentRepository.findById(1L),Optional.empty());

    // given

    Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);
    Mockito.when(itemRepository.findByItemId(Mockito.any())).thenReturn(testItem);

    // when
    Comment createdComment = commentService.createComment(testComment);

    // then
    assertEquals(testComment.getCommentId(), createdComment.getCommentId());
    assertEquals(testComment.getCommentOwnerId(), createdComment.getCommentOwnerId());
    assertEquals(testComment.getCommentItemId(), createdComment.getCommentItemId());
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
    Mockito.when(itemRepository.findByItemId(Mockito.any())).thenReturn(testItem);
    
    commentService.createComment(testComment);
    
    // if the user has already comment on this item, throws an exception
    assertThrows(ResponseStatusException.class, ()->commentService.createComment(testComment));
  }

  
  
}
