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
  @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
  private User user;

  @Column(nullable = false)
  private Long itemId;

  //ToDO: specify the foreign key mapping relationship to items
  /*
   * @ManyToOne
   * @JoinColumn()
   */

  @Column(nullable = false, length = 150)
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