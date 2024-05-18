package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @Column(nullable = false)
  private Long commentOwnerId;

  @Column(nullable = false)
  private String commentOwnerName;

  @Column(nullable = false)
  private Long commentItemId;

  /**
   * score to the item by the user
   */
//  @Column(nullable = false)
  @Column(nullable = true)
  private Long score;

  public static final int MAX_LENGTH = 250;
  
  @Column(nullable = true, columnDefinition = "TEXT", length = MAX_LENGTH)
  private String content;

  @Column(nullable = false)
  private Long thumbsUpNum = 0L;

  @ElementCollection
  @CollectionTable(name = "comment_liked_users", joinColumns = @JoinColumn(name = "comment_id"))
  @Column(name = "user_id", nullable = false)
  private List<Long> LikedUserList = new ArrayList<Long>();

  @Column
  private Long fatherCommentId;

  //methods
  public Long getCommentId(){
    return this.commentId;
  }

  public void setCommentId(Long commentId){
    this.commentId=commentId;
  }

  public Long getCommentOwnerId(){
    return this.commentOwnerId;
  }

  public void setCommentOwnerId(Long commentOwnerId){
    this.commentOwnerId = commentOwnerId;
  }

  public Long getCommentItemId(){
    return this.commentItemId;
  }

  public void setCommentItemId(Long commentItemId){
    this.commentItemId = commentItemId;
  }

  public String getCommentOwnerName() {
      return this.commentOwnerName;
  }

  public void setCommentOwnerName(String commentOwnerName) {
      this.commentOwnerName = commentOwnerName;
  }

  public Long getScore(){
    return this.score;
  }

  public void setScore(Long score){ 
    this.score=score;
  }

  public List<Long> getLikedUserList(){
    return this.LikedUserList;
  }

  public void setLikedUserList(List<Long> LikedUserList){
    this.LikedUserList=LikedUserList;
  }

  public String getContent(){
    return this.content;
  }

  public void setContent(String content){
    this.content=content;
  }

  public Long getThumbsUpNum(){
    return this.thumbsUpNum;
  }

  public void setThumbsUpNum(Long thumbsUpNum){
    this.thumbsUpNum=thumbsUpNum;
  }

  public Long getFatherCommentId() {
      return this.fatherCommentId;
  }

  public void setFatherCommentId(Long fatherCommentId) {
      this.fatherCommentId = fatherCommentId;
  }

}
