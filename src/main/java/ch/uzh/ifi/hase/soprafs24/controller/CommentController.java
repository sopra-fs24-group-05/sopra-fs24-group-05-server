package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Topic;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentStatusGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ReplyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.ItemService;
import ch.uzh.ifi.hase.soprafs24.service.TopicService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CommentController {

  private final CommentService commentService;

  private final UserService userService;

  private final ItemService itemService;

  private final TopicService topicService;

  @Autowired
  CommentController(CommentService commentService, UserService userService, ItemService itemService, TopicService topicService){
    this.commentService=commentService;
    this.userService=userService;
    this.itemService=itemService;
    this.topicService=topicService;
  }

  @GetMapping("/comments/commentId/{commentId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommentGetDTO getCommentByCommentId(@PathVariable Long commentId){
    Comment commentByCommentId = commentService.getCommentByCommentId(commentId);
    User commenOwner = userService.getUserById(commentByCommentId.getCommentOwnerId());
    Item commentItem = itemService.getItemByItemId(commentByCommentId.getCommentItemId());
    Topic commentItemTopic = topicService.getTopicById(commentItem.getTopicId());
    return DTOMapper.INSTANCE.converEntityToCommentGetDTO(commentByCommentId, commenOwner, commentItem, commentItemTopic);
  }


  @GetMapping("/comments/itemId/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<CommentGetDTO> getCommentsByCommentItemId(@PathVariable Long itemId){
      List<Comment> comments = commentService.getCommentByCommentItemId(itemId);
      List<CommentGetDTO> commentGetDTOs = new ArrayList<>();
      for(Comment comment : comments){
        User commentOwner = userService.getUserById(comment.getCommentOwnerId());
        Item commentItem = itemService.getItemByItemId(comment.getCommentItemId());
        Topic commentItemTopic = topicService.getTopicById(commentItem.getTopicId());
        commentGetDTOs.add(DTOMapper.INSTANCE.converEntityToCommentGetDTO(comment, commentOwner, commentItem, commentItemTopic));
      }
      return commentGetDTOs;
  }

  @PostMapping("/comments/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CommentGetDTO creatComment(@RequestBody CommentPostDTO commentPostDTO){
    Comment commentInput = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);
    Comment createdComment = commentService.createComment(commentInput);
    User commentOwner = userService.getUserById(createdComment.getCommentOwnerId());
    Item commentItem = itemService.getItemByItemId(createdComment.getCommentItemId());
    Topic commentItemTopic = topicService.getTopicById(commentItem.getTopicId());
    return DTOMapper.INSTANCE.converEntityToCommentGetDTO(createdComment, commentOwner, commentItem, commentItemTopic);
  }

  @GetMapping("/comments/userId/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<CommentGetDTO> getCommentByUserId(@PathVariable Long userId) {
    List<Comment> comments=commentService.getCommentByUserId(userId);
    List<CommentGetDTO> commentGetDTOs=new ArrayList<>();
    for(Comment comment:comments){
      System.out.println(comment.getContent());
      User commentOwner = userService.getUserById(userId);
      Item commentItem = itemService.getItemByItemId(comment.getCommentItemId());
      Topic commentItemTopic = topicService.getTopicById(commentItem.getTopicId());
      commentGetDTOs.add(DTOMapper.INSTANCE.converEntityToCommentGetDTO(comment, commentOwner, commentItem, commentItemTopic));
    }
    return commentGetDTOs;
  }

  
  @PutMapping("/comments/LikeComment/{commentId}/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommentStatusGetDTO addUserIdToLikedList(@PathVariable Long userId, @PathVariable Long commentId) {
    boolean isAlreadyLiked = commentService.checkUserLiked(userId, commentId);
    if(!isAlreadyLiked){
      commentService.addUserIdToLikedList(userId, commentId);
    }
    int thumbsUpNum = commentService.calculateThumbsUpNum(commentId);
    return DTOMapper.INSTANCE.converParamToCommentStatusGetDTO(isAlreadyLiked, thumbsUpNum);
  }

  @PostMapping("/reply/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public void createReply(@RequestBody CommentPostDTO replyPostDTO) {
    Comment replyToBeCreate = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(replyPostDTO);
    commentService.createReply(replyToBeCreate);
  }

  @GetMapping("/reply/get/{fatherCommentId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ReplyGetDTO> getReplyByFatherCommentId(@PathVariable Long fatherCommentId){
    List<Comment> replys=commentService.getReplyByFatherCommentId(fatherCommentId);
    List<ReplyGetDTO> replyGetDTOs=new ArrayList<>();
    for(Comment reply:replys){
      System.out.println(reply.getContent());
      User replyOwner = userService.getUserById(reply.getCommentOwnerId());
      replyGetDTOs.add(DTOMapper.INSTANCE.converEntityToReplyGetDTO(reply, replyOwner));
    }
    return replyGetDTOs;
  }
  
  
}
