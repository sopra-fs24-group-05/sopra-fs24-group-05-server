package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import ch.uzh.ifi.hase.soprafs24.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ChatController {

  private final ChatService chatService;

  ChatController(ChatService chatService){
    this.chatService=chatService;
  }

  // test not implemented
  @GetMapping("/chatMessage/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<MessageGetDTO> getChatMessagesByItemId(@PathVariable Long itemId) {
    List<ChatMessage> chatMessages = chatService.getChatMessagesByItemId(itemId);
    List<MessageGetDTO> messageGetDTOs = new ArrayList<>();
    for(ChatMessage chatMessage : chatMessages){
      messageGetDTOs.add(DTOMapper.INSTANCE.converChatMessageToMessageGetDTO(chatMessage));
    }
    return messageGetDTOs;
  }
  
}
