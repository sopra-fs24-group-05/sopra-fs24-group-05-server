package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;



@RestController
public class CommentController {

  private final CommentService commentService;

  CommentController(CommentService commentService){
    this.commentService=commentService;
  }

  @GetMapping("/comments/{commentId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Comment getCommentById(@PathVariable Long commentId){
    return commentService.getCommentById(commentId);
  }

  @GetMapping("/comments/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Comment> getCommentsByItemIdThumbsDesc(@PathVariable Long itemId){
    return commentService.getCommentByItemIdOrderByThumbsUpNumDesc(itemId, 0, 5);
  }

  @PostMapping("/comments/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CommentGetDTO creatComment(@RequestBody CommentPostDTO commentPostDTO){
    Comment commentInput = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);

    Comment createdComment = commentService.createComment(commentInput);
    return DTOMapper.INSTANCE.converEntityToCommentGetDTO(createdComment);
  }

  
}
