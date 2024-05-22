package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ChatRepository;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
public class ChatServiceTest {
  @Mock
  private ChatRepository chatRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ChatService chatService;

  @BeforeEach
  public void setup(){
    MockitoAnnotations.openMocks(this);
  }
  
  @AfterEach
  public void cleanup(){

  }
  
  @Test
  public void getChatMessagesByItemId_valid_success(){
    Long itemId = 1L;
    List<ChatMessage> expectedChatMessages = new ArrayList<>();
    expectedChatMessages.add(new ChatMessage());
    expectedChatMessages.get(0).setContent("Test message 1");
    expectedChatMessages.get(0).setUserId(1L);
    expectedChatMessages.get(0).setUserName("User1");
    expectedChatMessages.get(0).setUserAvatar("avatar1");
    expectedChatMessages.get(0).setMessageTime(LocalDate.now().toString());
    expectedChatMessages.get(0).setItemId(itemId);
    expectedChatMessages.add(new ChatMessage());
    expectedChatMessages.get(1).setContent("Test message 2");
    expectedChatMessages.get(1).setUserId(2L);
    expectedChatMessages.get(1).setUserName("User2");
    expectedChatMessages.get(1).setUserAvatar("avatar2");
    expectedChatMessages.get(1).setMessageTime(LocalDate.now().toString());
    expectedChatMessages.get(1).setItemId(itemId);

    Mockito.when(chatRepository.findByItemId(itemId)).thenReturn(expectedChatMessages);

    List<ChatMessage> actualChatMessages = chatService.getChatMessagesByItemId(itemId);

    // then
    assertEquals(expectedChatMessages.size(), actualChatMessages.size());
    for (int i = 0; i < expectedChatMessages.size(); i++) {
      assertEquals(expectedChatMessages.get(i).getMessageId(), actualChatMessages.get(i).getMessageId());
      assertEquals(expectedChatMessages.get(i).getContent(), actualChatMessages.get(i).getContent());
      assertEquals(expectedChatMessages.get(i).getItemId(), actualChatMessages.get(i).getItemId());
      assertEquals(expectedChatMessages.get(i).getUserId(), actualChatMessages.get(i).getUserId());
      assertEquals(expectedChatMessages.get(i).getUserName(), actualChatMessages.get(i).getUserName());
      assertEquals(expectedChatMessages.get(i).getMessageTime(), actualChatMessages.get(i).getMessageTime());
      assertEquals(expectedChatMessages.get(i).getUserAvatar(), actualChatMessages.get(i).getUserAvatar());
    }
  }

  @Test
  public void saveChatMessage_valid_success(){
    // given
    ChatMessage postMessage = new ChatMessage();
    postMessage.setContent("Test message");
    postMessage.setUserId(1L);
    //chatMessage.setUserName("User1");
    //chatMessage.setUserAvatar("avatar1");
    postMessage.setMessageTime(LocalDate.now().toString());
    postMessage.setItemId(1L);

    User user = new User();
    user.setUserId(1L);
    user.setUsername("User1");
    user.setAvatar("avatar1");
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // when
    ChatMessage savedMessage = chatService.saveChatMessage(postMessage);

    // then
    Mockito.verify(chatRepository,Mockito.times(1)).save(postMessage);

    assertEquals(postMessage.getContent(), savedMessage.getContent());
    assertEquals(postMessage.getItemId(), savedMessage.getItemId());
    assertEquals(user.getUserId(), savedMessage.getUserId());
    assertEquals(user.getUsername(), savedMessage.getUserName());
    assertEquals(postMessage.getMessageTime(), savedMessage.getMessageTime());
    assertEquals(user.getAvatar(), savedMessage.getUserAvatar());
  }

  
}
