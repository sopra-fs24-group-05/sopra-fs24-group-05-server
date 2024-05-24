package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TopicPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.ChatService;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TopicController.class)
@ActiveProfiles("test")
public class TopicControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private CommentService commentService;

  @MockBean
  private ItemService itemService;

  @MockBean
  private ChatService chatService;

  @Test
  public void createTopic_whenValidInput_thenReturnCreated() throws Exception {
      // given
      int topicId = 1;
      Topic topic = new Topic();
      topic.setTopicId(topicId);
      topic.setTopicName("Test Topic");
      topic.setDescription("This is a test topic");
      topic.setOwnerId(1);

      TopicPostDTO topicPostDTO = new TopicPostDTO();
      topicPostDTO.setTopicId(1);
      topicPostDTO.setTopicName("Test Topic");
      topicPostDTO.setDescription("This is a test topic");
      topicPostDTO.setOwnerId(1);

      given(topicService.createTopic(Mockito.any())).willReturn(topic);

      MockHttpServletRequestBuilder postRequest = post("/topics")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(topicPostDTO));

      mockMvc.perform(postRequest)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.id", is(topic.getTopicId())))
              .andExpect(jsonPath("$.topicName", is(topic.getTopicName())))
              .andExpect(jsonPath("$.description", is(topic.getDescription())))
              .andExpect(jsonPath("$.ownerId", is(topic.getOwnerId())));
  }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }


    @Test
    public void createTopic_already_exists() throws Exception {
        // given
        TopicPostDTO topicPostDTO = new TopicPostDTO();
        topicPostDTO.setTopicId(1);
        topicPostDTO.setTopicName("TestTopic");
        topicPostDTO.setDescription("This is a test topic");
        topicPostDTO.setOwnerId(1);


        given(topicService.createTopic(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "add User failed because topic name already exists"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(topicPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }



  @Test
  public void givenTopicId_whenGetTopics_thenReturnJsonArray() throws Exception {
    // given
      int topicId = 1;
      Topic topic = new Topic();
      topic.setTopicId(topicId);
      topic.setTopicName("Test Topic");
      topic.setDescription("This is a test topic");
      topic.setOwnerId(1);

    given(topicService.getTopicById(topicId)).willReturn(topic);

    // when
    MockHttpServletRequestBuilder getRequest = get("/topics/topicId/"+topicId)
            .contentType(MediaType.APPLICATION_JSON);


    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(topic.getTopicId())))
            .andExpect(jsonPath("$.topicName", is(topic.getTopicName())))
            .andExpect(jsonPath("$.description", is(topic.getDescription())))
            .andExpect(jsonPath("$.ownerId", is(topic.getOwnerId())));
  }

    @Test
    public void givenTopicId_whenGetTopic_thenFailed() throws Exception{
        // given
        given(topicService.getTopicById(2)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"topic with topicId was not found"));
//        given(topicService.getUserById(2L)).willReturn(null);

        // when
        MockHttpServletRequestBuilder getRequest = get("/topics/topicId/"+2).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test
    public void givenTopicName_whenGetTopics_thenReturnJsonArray() throws Exception {
        // given
        String topicName = "Test Topic";
        int topicId = 1;
        Topic topic = new Topic();
        topic.setTopicId(topicId);
        topic.setDescription("This is a test topic");
        topic.setOwnerId(1);

        given(topicService.getTopicByTopicName(topicName)).willReturn(topic);

        // when
        MockHttpServletRequestBuilder getRequest = get("/topics/topicName/"+topicName)
                .contentType(MediaType.APPLICATION_JSON);


        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(topic.getTopicId())))
                .andExpect(jsonPath("$.topicName", is(topic.getTopicName())))
                .andExpect(jsonPath("$.description", is(topic.getDescription())))
                .andExpect(jsonPath("$.ownerId", is(topic.getOwnerId())));
    }

    @Test
    public void givenTopicName_whenGetTopic_thenFailed() throws Exception{
        // given
        given(topicService.getTopicByTopicName("test Topic")).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"topic with topicName was not found"));
//        given(topicService.getUserById(2L)).willReturn(null);

        // when
        MockHttpServletRequestBuilder getRequest = get("/topics/topicName/"+"test Topic").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test()
    public void updateTopic_validInput_TopicUpdate() throws Exception {
        // given
        int topicId = 1;
        Topic topic = new Topic();
        topic.setTopicId(topicId);
        topic.setTopicName("Test Topic");
        topic.setDescription("This is a test topic");
        topic.setOwnerId(1);
        topic.setEditAllowed(false);

        Topic topicPutDTO = new Topic();
        topicPutDTO.setTopicId(1);
        topicPutDTO.setTopicName("Test Topic");
        topicPutDTO.setDescription("This is a test topic");
        topicPutDTO.setOwnerId(1);
        topicPutDTO.setEditAllowed(false);

        given(topicService.updateTopic(Mockito.any())).willReturn(topic);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/topics/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(topicPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(topic.getTopicId())))
                .andExpect(jsonPath("$.topicName", is(topic.getTopicName())))
                .andExpect(jsonPath("$.description", is(topic.getDescription())))
                .andExpect(jsonPath("$.ownerId", is(topic.getOwnerId())))
                .andExpect(jsonPath("$.editAllowed", is(topic.getEditAllowed())));
    }


    @Test
    public void deleteTopicWithName_validInput_TopicDeleted() throws Exception {

        String topicName = "Test Topic";

        BDDMockito.willDoNothing().given(topicService).deleteTopicByTopicName(anyString());

        MockHttpServletRequestBuilder deleteRequest = delete("/topics/topicName/{topicName}", topicName)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent()); // 204 No Content

        verify(topicService, times(1)).deleteTopicByTopicName(topicName);
    }


    @Test
    public void deleteTopicWithId_validInput_TopicDeleted() throws Exception {

        Integer topicId = 1;

        BDDMockito.willDoNothing().given(topicService).deleteTopicByTopicId(topicId);

        MockHttpServletRequestBuilder deleteRequest = delete("/topics/topicId/{topicId}", topicId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent()); // 204 No Content

        verify(topicService, times(1)).deleteTopicByTopicId(topicId);
    }

    @Test
    public void getPopularTopics_validInput_TopicsListReturned() throws Exception {
        // given
        Topic topic1 = new Topic();
        topic1.setTopicName("Test Topic 1");
        Topic topic2 = new Topic();
        topic2.setTopicName("Test Topic 2");
        List<Topic> popularTopics = Arrays.asList(topic1, topic2);

        when(topicService.getMostPopularTopics()).thenReturn(popularTopics);

        // when/then
        mockMvc.perform(get("/topics/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].topicName", is("Test Topic 1")))
                .andExpect(jsonPath("$[1].topicName", is("Test Topic 2")));
    }

    @Test
    public void initializeTopics_whenCalled_thenTopicsInitialized() throws Exception {
        // Given
        doNothing().when(topicService).initializeTopics();

        // When & Then
        mockMvc.perform(post("/topics/initialize")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(topicService).initializeTopics();
    }
  }

