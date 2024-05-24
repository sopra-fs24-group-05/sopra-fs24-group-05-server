package ch.uzh.ifi.hase.soprafs24.component;

import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessagePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ChatService;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/WebServer/{itemId}/{userId}")
@Component
public class WebSocketServer{

  private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

  @Autowired
  public void setApplicationContext(ChatService chatService, UserService userService){
    WebSocketServer.chatService = chatService;
    WebSocketServer.userService = userService;
    log.info("ChatService injected successfully: {}", chatService != null);
  }

  private static ChatService chatService;
  private static UserService userService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  // 
  public WebSocketServer(){  }

  /**
   * keep track of current number of connections
   */
  public static final Map<Long, Set<Session>> groupSessionMap = new ConcurrentHashMap<>();
  public static final Map<Session, Long> userMap = new ConcurrentHashMap<>();

  private Long currentItemId;

  // called when connection built
  @OnOpen
  public void onOpen(Session session, @PathParam("itemId")Long itemId, @PathParam("userId")Long userId){
    // some initialization operations when the connection is established
    groupSessionMap.computeIfAbsent(itemId, k ->ConcurrentHashMap.newKeySet()).add(session); // if there is no groupSessionMap with key "itemId", create one and add session to the set
    userMap.put(session, userId);
    currentItemId = itemId;
    log.info("new connection to group chat (itemId = {}) established, userId = {}, current number of connections = {}", currentItemId, userId, groupSessionMap.get(itemId).size());
  }

  @OnClose
  public void onClose(Session session,@PathParam("itemId")Long itemId, @PathParam("userId")Long userId){
    if(groupSessionMap.containsKey(itemId) && !groupSessionMap.get(itemId).isEmpty()){
      groupSessionMap.get(itemId).remove(session);
      log.info("connection remove from group chat (itemId = {}) for userID = {}, current number of connections = {}", currentItemId, userId, groupSessionMap.get(itemId).size());
      if(groupSessionMap.get(itemId).isEmpty()){
        groupSessionMap.remove(itemId);
      }
    }
    log.info("No connection to group chat (itemId = {}), group char closed", itemId);
  }

    // called when recieve message from client / client send message
    @OnMessage 
    public void onMessage(Session session, String message){
      try{
        log.info("group char (itemId = {}) recieve message:{}", currentItemId, message);
        MessagePostDTO messagePostDTO = objectMapper.readValue(message, MessagePostDTO.class);
        ChatMessage chatMessage = DTOMapper.INSTANCE.converMessagePostDTOToChatMessage(messagePostDTO);
        chatService.saveChatMessage(chatMessage);
        User chatMessagOwner = userService.getUserById(chatMessage.getUserId());
        sendMessageToAll(DTOMapper.INSTANCE.converChatMessageToMessageGetDTO(chatMessage, chatMessagOwner));

      } catch (IOException e){
        // error decoding JSON
        log.error("fail to decode message", e);
      }
    }

  @OnError
  public void onError(Session session, Throwable throwable){
    log.info("WebSocket error for session {}, userId = {}", session.getId(), userMap.get(session), throwable);
  }

  private void sendMessage(String message, Session session){

  }

  private void sendMessageToAll(MessageGetDTO messageGetDTO){
    try{
      String jsonMessage = objectMapper.writeValueAsString(messageGetDTO);
      for(Session session : groupSessionMap.get(currentItemId)){
        log.info("send to client {}, message = {}", userMap.get(session), jsonMessage);
        session.getBasicRemote().sendText(jsonMessage);
      }
    } catch(Exception e){
      log.error("sending message to client fail", e);
    }
  }
  
}
