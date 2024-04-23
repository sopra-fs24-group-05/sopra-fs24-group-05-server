package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.*;
//import org.springframework.http.HttpCookie; // ToDO: figure out how to use cookie to manage token and other information
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CommentService {

  private final Logger log = LoggerFactory.getLogger(CommentService.class);

  private final CommentRepository commentRepository;

  @Autowired
  public CommentService(@Qualifier("commentRepository") CommentRepository commentRepository){
    this.commentRepository = commentRepository;
  }

  public Comment getCommentById(Long commentId){
    if(!commentRepository.existsById(commentId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"comment not found");
    }
    return commentRepository.findById(commentId).get();
  }

  public List<Comment> getCommentOrderByThumbsUpNumDesc(int pageNumber, int pageSize){
    Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by("thumbsUpNum").descending());
    return commentRepository.findByOrderByThumbsUpNumDesc(pageable);
  }

  /*
   * another implement way
   * ToDo: if we want to display many comments and distribute them every n comments each page
   * maybe we should implement this way
   * 
   * public List<Comment> getCommentOrderByThumbsUpNumDesc(int pageNumber, int pageSize){
    Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by("thumbsUpNum").descending());
    Page<Comment> page=commentRepository.findAll(pageable);
    return page.getContent();
  }
   */

   public Comment creatComment(Comment newComment) throws ResponseStatusException{
    if(newComment.getContent().length()>newComment.MAX_LENGTH){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The comments exceeded the 150-character limit");
    }
    newComment = commentRepository.saveAndFlush(newComment);
    return newComment;
   }


  
}
