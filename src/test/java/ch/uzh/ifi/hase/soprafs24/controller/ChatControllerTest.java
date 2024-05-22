package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ChatService;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

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

  private ChatMessage chatMessage1;
  private ChatMessage chatMessage2;

  @BeforeEach
  public void setup() {
    chatMessage1 = new ChatMessage();
    chatMessage1.setMessageId(1L);
    chatMessage1.setItemId(1L);
    chatMessage1.setUserId(1L);
    chatMessage1.setUserName("User1");
    chatMessage1.setUserAvatar("Avatar1");
    chatMessage1.setContent("First chat message");
    chatMessage1.setMessageTime(LocalDate.now().toString());

    chatMessage2 = new ChatMessage();
    chatMessage2.setMessageId(2L);
    chatMessage2.setItemId(1L);
    chatMessage2.setUserId(2L);
    chatMessage2.setUserName("User2");
    chatMessage2.setUserAvatar("Avatar2");
    chatMessage2.setContent("Second chat message");
    chatMessage2.setMessageTime(LocalDate.now().toString());
  }

  @Test
  public void getChatMessagesByItemId_success() throws Exception {
      List<ChatMessage> chatMessages = Arrays.asList(chatMessage1, chatMessage2);
      given(chatService.getChatMessagesByItemId(1L)).willReturn(chatMessages);

      MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/chatMessage/1")
              .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(getRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$", hasSize(2)))
              .andExpect(jsonPath("$[0].messageId", is(chatMessage1.getMessageId().intValue())))
              .andExpect(jsonPath("$[0].content", is(chatMessage1.getContent())))
              .andExpect(jsonPath("$[0].userId", is(chatMessage1.getUserId().intValue())))
              .andExpect(jsonPath("$[0].userName", is(chatMessage1.getUserName())))
              .andExpect(jsonPath("$[0].userAvatar", is(chatMessage1.getUserAvatar())))
              .andExpect(jsonPath("$[0].messageTime", is(chatMessage1.getMessageTime())))
              .andExpect(jsonPath("$[1].messageId", is(chatMessage2.getMessageId().intValue())))
              .andExpect(jsonPath("$[1].content", is(chatMessage2.getContent())))
              .andExpect(jsonPath("$[1].userId", is(chatMessage2.getUserId().intValue())))
              .andExpect(jsonPath("$[1].userName", is(chatMessage2.getUserName())))
              .andExpect(jsonPath("$[1].userAvatar", is(chatMessage2.getUserAvatar())))
              .andExpect(jsonPath("$[1].messageTime", is(chatMessage2.getMessageTime())));
  }

  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
