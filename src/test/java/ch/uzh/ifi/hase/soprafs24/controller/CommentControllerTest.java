package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CommentControllerTest
 */

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CommentService commentService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private ItemService itemService;

  @MockBean 
  private UserService userService;

  private Comment comment;

  @BeforeEach
  public void setup(){
    comment = new Comment();
    comment.setCommentId(1L);
    comment.setCommentOwnerId(1L);
    comment.setCommentOwnerName("commentOwner");
    comment.setCommentItemId(1L);
    comment.setScore(5L);
    comment.setContent(null);
    comment.setThumbsUpNum(1L);
  }

  @Test
  public void createComment_validInput_commentCreated() throws Exception{
    //given

    CommentPostDTO commentPostDTO = new CommentPostDTO();
    commentPostDTO.setCommentId(1L);
    commentPostDTO.setCommentOwnerId(1L);
    commentPostDTO.setCommentItemId(1L);

    given(commentService.createComment(Mockito.any())).willReturn(comment);

    MockHttpServletRequestBuilder postRequest = post("/comments/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(commentPostDTO));

    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.commentId", is(comment.getCommentId().intValue())))
        .andExpect(jsonPath("$.commentOwnerId", is(comment.getCommentOwnerId().intValue())))
        .andExpect(jsonPath("$.commentItemId", is(comment.getCommentItemId().intValue())))
        .andExpect(jsonPath("$.score", is(comment.getScore().intValue())))
        .andExpect(jsonPath("$.content", is(comment.getContent())))
        .andExpect((jsonPath("$.thumbsUpNum", is(comment.getThumbsUpNum().intValue()))));
  }

  @Test
  public void givenComments_whenGetCommentByUserId_thenReturnJsonArray() throws Exception{
    //given：some comments have been created 

    List<Comment> commentsByUserId = Collections.singletonList(comment);

    given(commentService.getCommentByUserId(Mockito.anyLong())).willReturn(commentsByUserId);

    MockHttpServletRequestBuilder getRequest = get("/comments/userId/{userId}",1L)
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(commentsByUserId));

    mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].commentId", is(comment.getCommentId().intValue())))
      .andExpect(jsonPath("$[0].commentItemId", is(comment.getCommentItemId().intValue())))
      .andExpect(jsonPath("$[0].score", is(comment.getScore().intValue())));    
  }

  @Test
  public void givenComments_whenGetCommentByItemId_thenReturnJsonArray() throws Exception{
    //given：some comments have been created 

    List<Comment> commentsByItemId = Collections.singletonList(comment);

    given(commentService.getCommentByCommentItemId(Mockito.anyLong())).willReturn(commentsByItemId);

    MockHttpServletRequestBuilder getRequest = get("/comments/itemId/{itemId}",1L)
      .contentType(MediaType.APPLICATION_JSON);
      //.content(asJsonString(commentsByItemId));

    mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].commentId", is(comment.getCommentId().intValue())))
      .andExpect(jsonPath("$[0].commentItemId", is(comment.getCommentItemId().intValue())))
      .andExpect(jsonPath("$[0].score", is(comment.getScore().intValue())));    
  }

  @Test
  public void givenComments_whenGetCommentByCommentId_thenReturnJsonArray() throws Exception{
    //given：some comments have been created 

    given(commentService.getCommentByCommentId(Mockito.anyLong())).willReturn(comment);

    MockHttpServletRequestBuilder getRequest = get("/comments/commentId/{commentId}",1L)
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(comment));

    mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.commentId", is(comment.getCommentId().intValue())))
      .andExpect(jsonPath("$.commentItemId", is(comment.getCommentItemId().intValue())))
      .andExpect(jsonPath("$.score", is(comment.getScore().intValue())));    
  }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
  
}
