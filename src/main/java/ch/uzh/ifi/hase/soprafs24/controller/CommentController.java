package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class CommentController {

  private final CommentService commentService;

  @Autowired
  CommentController(CommentService commentService){
    this.commentService=commentService;
  }

  @GetMapping("/comments/commentId/{commentId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommentGetDTO getCommentByCommentId(@PathVariable Long commentId){
    Comment commentByCommentId = commentService.getCommentByCommentId(commentId);
    return DTOMapper.INSTANCE.converEntityToCommentGetDTO(commentByCommentId);
  }

//  @GetMapping("/comment/{itemId}")
//  @ResponseStatus(HttpStatus.OK)
//  @ResponseBody
//  public List<CommentGetDTO> getCommentsByItemIdThumbsDesc(@PathVariable Long itemId){
//    List<Comment> comments=commentService.getCommentByItemIdOrderByThumbsUpNumDesc(itemId, 0, 5);
//    List<CommentGetDTO> commentGetDTOs=new ArrayList<>();
//    for(Comment comment:comments){
//      commentGetDTOs.add(DTOMapper.INSTANCE.converEntityToCommentGetDTO(comment));
//    }
//    return commentGetDTOs;
//  }

  @GetMapping("/comments/itemId/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<CommentGetDTO> getCommentsByCommentItemId(@PathVariable Long itemId){
      List<Comment> comments = commentService.getCommentByCommentItemId(itemId);
      List<CommentGetDTO> commentGetDTOs = new ArrayList<>();
      for(Comment comment : comments){
          commentGetDTOs.add(DTOMapper.INSTANCE.converEntityToCommentGetDTO(comment));
      }
      return commentGetDTOs;
  }

  @PostMapping("/comments/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CommentGetDTO creatComment(@RequestBody CommentPostDTO commentPostDTO){
    Comment commentInput = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);

    Comment createdComment = commentService.createComment(commentInput);
    return DTOMapper.INSTANCE.converEntityToCommentGetDTO(createdComment);
  }

  @GetMapping("/comments/userId/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<CommentGetDTO> getCommentByUserId(@PathVariable Long userId) {
    List<Comment> comments=commentService.getCommentByUserId(userId);
    List<CommentGetDTO> commentGetDTOs=new ArrayList<>();
    for(Comment comment:comments){
      System.out.println(comment.getContent());
      commentGetDTOs.add(DTOMapper.INSTANCE.converEntityToCommentGetDTO(comment));
    }
    return commentGetDTOs;
  }
  
}
