package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
import static org.hamcrest.Matchers.equalTo;
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

  @Test
  public void createComment_validInput_commentCreated() throws Exception{
    //given
    Comment comment = new Comment();
    comment.setCommentId(1L);
    comment.setUserId(1L);
    comment.setItemId(1L);
    comment.setScore(5L);
    comment.setContent(null);
    comment.setThumbsUpNum(1L);

    CommentPostDTO commentPostDTO = new CommentPostDTO();
    commentPostDTO.setCommentId(1L);
    commentPostDTO.setCUserId(1L);
    commentPostDTO.setItemId(1L);

    given(commentService.createComment(Mockito.any())).willReturn(comment);

    MockHttpServletRequestBuilder postRequest = post("/comments/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(commentPostDTO));

    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.commentId", is(comment.getCommentId().intValue())))
        .andExpect(jsonPath("$.userId", is(comment.getUserId().intValue())))
        .andExpect(jsonPath("$.itemId", is(comment.getItemId().intValue())))
        .andExpect(jsonPath("$.score", is(comment.getScore().intValue())))
        .andExpect(jsonPath("$.content", is(comment.getContent())))
        .andExpect((jsonPath("$.thumbsUpNum", is(comment.getThumbsUpNum().intValue()))));

  }

  @Test
  public void givenComments_whenGetCommentByUserId_thenReturnJsonArray() throws Exception{
    //given：some comments have been created 
    Comment comment = new Comment();
    comment.setCommentId(1L);
    comment.setUserId(1L);
    comment.setItemId(1L);
    comment.setScore(5L);
    comment.setContent(null);
    comment.setThumbsUpNum(1L);

    List<Comment> commentsByUserId = Collections.singletonList(comment);

    given(commentService.getCommentByUserId(Mockito.anyLong())).willReturn(commentsByUserId);

    MockHttpServletRequestBuilder getRequest = get("/comments/findByUserId/{userId}",1L)
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(commentsByUserId));

    mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].commentId", is(comment.getCommentId().intValue())))
      .andExpect(jsonPath("$[0].itemId", is(comment.getItemId().intValue())))
      .andExpect(jsonPath("$[0].score", is(comment.getScore().intValue())));    
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
