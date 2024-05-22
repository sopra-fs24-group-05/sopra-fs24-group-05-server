package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.repository.ChatRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TopicRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class ChatService {
  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final ChatRepository chatRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final TopicRepository topicRepository;

  @Autowired
  public ChatService(@Qualifier("userRepository") UserRepository userRepository, 
  @Qualifier("itemRepository") ItemRepository itemRepository, 
  @Qualifier("topicRepository") TopicRepository topicRepository,
  @Qualifier("chatRepository") ChatRepository chatRepository) {
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
    this.topicRepository = topicRepository;
    this.chatRepository = chatRepository;
  }

  public List<ChatMessage> getChatMessagesByItemId(Long itemId){
    return chatRepository.findByItemId(itemId);
  }

  public ChatMessage saveChatMessage(ChatMessage chatMessage){
    User userbyId = userRepository.findById(chatMessage.getUserId()).orElseThrow();
    chatMessage.setUserAvatar(userbyId.getAvatar()); 
    chatMessage.setUserName(userbyId.getUsername());
    chatMessage.setMessageTime(LocalDate.now().toString());
    checkMessageFieldsIsNull(chatMessage);
    chatRepository.save(chatMessage);
    chatRepository.flush();
    return chatMessage;
  }

  private void checkMessageFieldsIsNull(ChatMessage chatMessage){
    
  }
}
