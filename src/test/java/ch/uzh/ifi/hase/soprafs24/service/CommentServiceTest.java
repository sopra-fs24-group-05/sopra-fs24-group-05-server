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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

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
    testComment.setUserId(1L);
    testComment.setItemId(1L);
    testComment.setScore(5L);
    testComment.setContent("test content");
    testComment.setThumbsUpNum(1L);

    testCommentList = Collections.singletonList(testComment);

    optionalTestComment = Optional.of(testComment);

    testItem = new Item();
    testItem.setId(1L);
    testItem.setLikes(0);
    testItem.setName("testItemName");
    testItem.setScore(0.0);
    testItem.setDescription("test Item Description");

    Mockito.when(commentRepository.save(Mockito.any())).thenReturn(testComment);
    Mockito.when(commentRepository.existsByUserIdAndItemId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
    Mockito.when(commentRepository.calculateAverageScoreByItemId(Mockito.any())).thenReturn(testComment.getScore().doubleValue());
    Mockito.when(commentRepository.existsByItemId(Mockito.anyLong())).thenReturn(true);
    Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(optionalTestComment);
    Mockito.when(commentRepository.findByUserId(Mockito.anyLong())).thenReturn(testCommentList);
    //Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);
  }

  @Test
  public void createComment_validInput_success(){
    Mockito.when(itemRepository.existsById(Mockito.any())).thenReturn(true);

    Comment createdComment = commentService.createComment(testComment);

    Mockito.verify(commentRepository,Mockito.times(1)).save(Mockito.any());// commentRepository.save() has been called once

    assertEquals(testComment.getCommentId(), createdComment.getCommentId());
    assertEquals(testComment.getUserId(), createdComment.getUserId());
    assertEquals(testComment.getItemId(), createdComment.getItemId());
    assertEquals(testComment.getScore(), createdComment.getScore());
    assertEquals(testComment.getContent(), createdComment.getContent());
    assertEquals(testComment.getThumbsUpNum(), createdComment.getThumbsUpNum());
  }

  @Test
  public void calculateAverageScoreByItemId_validInput_success(){
    Double avgScore = commentService.calculateAverageScoreByItemId(testComment.getItemId());

    Mockito.verify(commentRepository,Mockito.times(1)).existsByItemId(Mockito.any());
    Mockito.verify(commentRepository,Mockito.times(1)).calculateAverageScoreByItemId(Mockito.any());

    assertEquals(avgScore, testComment.getScore().doubleValue());
  }

  @Test
  public void getCommentByCommentId_validInput_success(){
    Mockito.when(commentRepository.existsById(Mockito.any())).thenReturn(true);
    Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(optionalTestComment);
    
    Comment getByCommentItComment = commentService.getCommentByCommentId(testComment.getCommentId());

    Mockito.verify(commentRepository,Mockito.times(1)).findById(Mockito.anyLong());

    assertEquals(testComment.getCommentId(), getByCommentItComment.getCommentId());
    assertEquals(testComment.getUserId(), getByCommentItComment.getUserId());
    assertEquals(testComment.getItemId(), getByCommentItComment.getItemId());
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
}
