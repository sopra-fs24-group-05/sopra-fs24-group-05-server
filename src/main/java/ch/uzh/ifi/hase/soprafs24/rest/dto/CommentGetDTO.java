package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class CommentGetDTO {

  private Long commentId;
  private Long userId;
  private Long itemId;
  private String content;
  private Long thumbsUpNum;

  // Get & set methods
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
