package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

@Entity
@Table(name = "COMMENTS")
public class Comment {

  @Id
  @GeneratedValue
  private Long commentId;

  @Column(nullable = false)
  private Long userId;

  // specify the foreign key mapping relationship
  // no need to implement get() & set() methods, @ManyToOne provide a getUser() methods
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
  private User user;

  @Column(nullable = false)
  private Long itemId;

  @ManyToOne
  @JoinColumn(name = "itemId", referencedColumnName = "id", insertable = false, updatable = false)
  private Item item;

  /**
   * score to the item by the user
   */
  @Column(nullable = false)
  private Long score;

  public static final int MAX_LENGTH = 150;
  
  @Column(nullable = true, length = MAX_LENGTH)
  private String content;

  @Column(nullable = false)
  private Long thumbsUpNum;

  //methods
  public Long getCommentId(){
    return this.commentId;
  }

  public void setCommentId(Long commentId){
    this.commentId=commentId;
  }

  public Long getUserId(){
    return this.userId;
  }

  public void setUserId(Long userId){
    this.userId=userId;
  }

  public Long getItemId(){
    return this.itemId;
  }

  public void setItemId(Long ItemId){ 
    this.itemId=itemId;
  }

  public Long getScore(){
    return this.score;
  }

  public void setScore(Long score){ 
    this.score=score;
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

}
