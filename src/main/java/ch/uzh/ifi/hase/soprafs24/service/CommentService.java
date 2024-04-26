package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
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
  private final ItemRepository itemRepository;

  @Autowired
  public CommentService(@Qualifier("commentRepository") CommentRepository commentRepository,
                        @Qualifier("itemRepository" ) ItemRepository itemRepository){
    this.commentRepository = commentRepository;
    this.itemRepository = itemRepository;
  }

  public Comment getCommentByCommentId(Long commentId){
    if(!commentRepository.existsById(commentId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"comment not found");
    }
    return commentRepository.findById(commentId).get();
  }

  public List<Comment> getCommentByItemIdOrderByThumbsUpNumDesc(Long itemId, int pageNumber, int pageSize){
    Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by("thumbsUpNum").descending());
    return commentRepository.findByItemIdOrderByThumbsUpNumDesc(itemId,pageable);
  }

  public List<Comment> getCommentByUserId(Long userId){
    if(!commentRepository.existsByUserId(userId)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"comment not found");
    }
    return commentRepository.findByUserId(userId);
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
  /**
   * Service methods for creating Comment
   * check if the content exceed max length
   * check if the user has already comment on the item
   * @param newComment
   * @return newComment
   * @throws ResponseStatusException
   */
  public Comment createComment(Comment newComment) throws ResponseStatusException{
    if(!itemRepository.existsById(newComment.getItemId())){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
    }
    if(newComment.getContent().length()>newComment.MAX_LENGTH){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The comments exceeded the 150-character limit");
    }
    boolean hasCommented = commentRepository.existsByUserIdAndItemId(newComment.getUserId(),newComment.getItemId());
    if(hasCommented){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The User has already commented on this item");
    }
    newComment.setThumbsUpNum(0L);
    newComment = commentRepository.save(newComment);
    commentRepository.flush();
    //newComment = commentRepository.saveAndFlush(newComment); // .save will be called twice? in test if we wrote this
    return newComment;
  }

  public Double calculateAverageScoreByItemId(Long itemId){
    if(!commentRepository.existsByItemId(itemId)){
      return 0.0;
    }
    return commentRepository.calculateAverageScoreByItemId(itemId);
  }

}
