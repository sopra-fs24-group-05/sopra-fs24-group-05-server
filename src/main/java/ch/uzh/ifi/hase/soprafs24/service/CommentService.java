package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentService {

  private final Logger log = LoggerFactory.getLogger(CommentService.class);

  private final CommentRepository commentRepository;
  private final ItemRepository itemRepository;

  @Autowired
  public CommentService(@Qualifier("commentRepository") CommentRepository commentRepository,
                        @Qualifier("itemRepository" ) ItemRepository itemRepository, @Qualifier("userRepository" ) UserRepository userRepository){
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
    return commentRepository.findByCommentItemIdOrderByThumbsUpNumDesc(itemId,pageable);
  }

  public List<Comment> getCommentByCommentItemId(Long commentItemId) {
    List<Comment> commentList = commentRepository.findByCommentItemId(commentItemId);
    if (commentList == null) {
        throw new RuntimeException("Comment Not Found");
    } else {
        System.out.println(commentList.size());
        return commentList;
    }
  }

  public List<Comment> getCommentByUserId(Long userId){
    if(!commentRepository.existsByCommentOwnerId(userId)){
//      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"comment not found");
        return new ArrayList<>();
    }
    return commentRepository.findByCommentOwnerId(userId);
  }

  public List<Comment> getReplyByFatherCommentId(Long fatherCommentId){
    // todo
    return commentRepository.findByFatherCommentId(fatherCommentId);
  }

  /**
   * Service methods for creating Comment
   * check if the content exceed max length
   * check if the user has already comment on the item
   * @param newComment
   * @return newComment
   * @throws ResponseStatusException
   */
  public Comment createComment(Comment newComment) throws ResponseStatusException{
    if(!itemRepository.existsById(newComment.getCommentItemId())){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
    }
    if(newComment.getContent().length()>newComment.MAX_LENGTH){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The comments exceeded the 250-character limit");
    }
    boolean hasCommented = commentRepository.existsByCommentOwnerIdAndCommentItemId(newComment.getCommentOwnerId(),newComment.getCommentItemId());
    if(hasCommented){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The User has already commented on this item");
    }

    Item itemOfComment = itemRepository.findByItemId(newComment.getCommentItemId());
    //newComment.setCommentOwnerName(newComment.getCommentOwnerName());
    newComment.setThumbsUpNum(0L);
    newComment = commentRepository.save(newComment);
    commentRepository.flush();
    itemOfComment.setScore(commentRepository.calculateAverageScoreByCommentItemId(newComment.getCommentItemId()));
    itemRepository.save(itemOfComment);
    itemRepository.flush();
    return newComment;
  }

  public void createReply(Comment reply) throws ResponseStatusException{
    if(!itemRepository.existsById(reply.getCommentItemId())){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
    }
    if(reply.getContent().length()>reply.MAX_LENGTH){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The reply exceeded the 250-character limit");
    }
    if(reply.getFatherCommentId()==null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fatherCommentId missing");
    }
    commentRepository.save(reply);
    commentRepository.flush();
  }

  public Double calculateAverageScoreByItemId(Long itemId){
    if(!commentRepository.existsByCommentItemId(itemId)){
      return 0.0;
    }
    return commentRepository.calculateAverageScoreByCommentItemId(itemId);
  }

  public boolean checkUserLiked(Long UserId, Long CommentId){
    Comment commentById = commentRepository.findById(CommentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment Not found"));
    return commentById.getLikedUserList().contains(UserId);
  }

  public int calculateThumbsUpNum(Long CommentId){
    Comment commentById = commentRepository.findById(CommentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment Not found"));
    return commentById.getLikedUserList().size();
  }

  public void addUserIdToLikedList(Long UserId, Long CommentId){
    Comment commentById = commentRepository.findById(CommentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment Not found"));
    List<Long> LikedUserList = commentById.getLikedUserList();
    if(checkUserLiked(UserId, CommentId)){
      return;
    }
    LikedUserList.add(UserId);
    commentById.setThumbsUpNum(Long.valueOf(LikedUserList.size()));
    commentById.setLikedUserList(LikedUserList);
    commentRepository.save(commentById);
    commentRepository.flush();
  }

}
