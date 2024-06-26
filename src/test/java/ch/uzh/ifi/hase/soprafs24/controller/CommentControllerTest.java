package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

  @MockBean
  private ChatService chatService;

  private Comment comment;
  private Comment reply;
  private User user;
  private Item commentItem;
  private Topic commentItemTopic;

  @BeforeEach
  public void setup(){
    comment = new Comment();
    comment.setCommentId(1L);
    comment.setCommentOwnerId(1L);
    comment.setCommentOwnerName("commentOwner");
    comment.setCommentItemId(1L);
    comment.setScore(5L);
    comment.setContent("null");
    comment.setThumbsUpNum(1L);
    comment.setLikedUserList(new ArrayList<Long>(Arrays.asList(1L, 2L, 3L)));

    reply = new Comment();
    reply.setCommentId(1L);
    reply.setCommentOwnerId(1L);
    reply.setCommentOwnerName("replyOwner");
    reply.setCommentItemId(1L);
    reply.setScore(5L);
    reply.setContent("null");
    reply.setThumbsUpNum(1L);
    reply.setLikedUserList(new ArrayList<>(Arrays.asList(1L, 2L, 3L)));
    reply.setFatherCommentId(comment.getCommentId());

    user = new User();
    user.setAvatar("test ");
    user.setUsername("test username");
    user.setUserId(1L);

    commentItem = new Item();
    commentItem.setItemId(1L);;
    commentItem.setItemName("test comment ItemName");
    commentItem.setTopicId(1);

    commentItemTopic = new Topic();
    commentItemTopic.setTopicId(1);
    commentItemTopic.setTopicName("test comment ItemTopicName");

    given(userService.getUserById(Mockito.anyLong())).willReturn(user);
    given(itemService.getItemByItemId(Mockito.anyLong())).willReturn(commentItem);
    given(topicService.getTopicById(Mockito.anyInt())).willReturn(commentItemTopic);
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
  public void createReply_validInput_replyCreated() throws Exception {
    // given
    CommentPostDTO replyPostDTO = new CommentPostDTO();
    replyPostDTO.setCommentOwnerId(1L);
    replyPostDTO.setCommentOwnerName("replyOwner");
    replyPostDTO.setCommentItemId(1L);

    doNothing().when(commentService).createReply(Mockito.any());
    //doNothing().when(commentService.createReply(Mockito.any())).willReturn(reply);

    MockHttpServletRequestBuilder postRequest = post("/reply/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(replyPostDTO));

    // when
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated());
        
    Mockito.verify(commentService, Mockito.times(1)).createReply(Mockito.any());
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

  @Test
  public void LikeComments_validInput_commentStatusReturned() throws Exception {
    // set requested userId(not in LikedUserList), commentId and expected result
    Long userId = 4L;
    Long commentId = 1L;
    boolean isAlreadyLiked = false;
    int thumbsUpNum = 2;

    given(commentService.checkUserLiked(userId, commentId)).willReturn(isAlreadyLiked);
    given(commentService.calculateThumbsUpNum(commentId)).willReturn(thumbsUpNum);

    // Mock the request object
    MockHttpServletRequestBuilder putRequest = put("/comments/LikeComment/{commentId}/{userId}", commentId, userId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc.perform(putRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isAlreadyLiked").value(isAlreadyLiked))
            .andExpect(jsonPath("$.thumbsUpNum").value(thumbsUpNum));
  }

  @Test
  public void givenReplies_whenGetReplyByFatherCommentId_thenReturnJsonArray() throws Exception {
    List<Comment> replies = Collections.singletonList(reply);

    given(commentService.getReplyByFatherCommentId(Mockito.anyLong())).willReturn(replies);

    MockHttpServletRequestBuilder getRequest = get("/reply/get/{fatherCommentId}", 1L)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].commentId", is(reply.getCommentId().intValue())))
            .andExpect(jsonPath("$[0].commentOwnerId", is(reply.getCommentOwnerId().intValue())))
            .andExpect(jsonPath("$[0].commentOwnerName", is(user.getUsername())))
            .andExpect(jsonPath("$[0].content", is(reply.getContent())));
  }

  @Test
  public void deleteCommentOrReply_validInput_success() throws Exception {
      // Mocking the service call
      doNothing().when(commentService).deleteCommentOrReply(Mockito.anyLong(), Mockito.anyLong());

      // Building the delete request
      MockHttpServletRequestBuilder deleteRequest = delete("/comments/delete/1")
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(1L)); // Here commentId is 1

      // Performing the delete request and verifying the response
      mockMvc.perform(deleteRequest)
          .andExpect(status().isNoContent());

      // Verifying that the service method was called once with the expected parameters
      verify(commentService, times(1)).deleteCommentOrReply(1L, 1L);
  }

  
  
}
