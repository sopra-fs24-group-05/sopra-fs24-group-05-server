package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ReplyGetDTO {

  private Long commentId;
  private Long commentOwnerId;
  private String commentOwnerName;
  private Long commentItemId;
  private Long fatherCommentId;
  //private Long score;
  private String content;
  //private Long thumbsUpNum;
  private String commentOwnerAvatar;

  public void setCommentId(Long commentId){
    this.commentId = commentId;
  }
  public Long getCommentId(){
    return this.commentId;
  }

  public void setCommentOwnerId(Long commentOwnerId){
    this.commentOwnerId = commentOwnerId;
  }
  public Long getCommentOwnerId(){
    return this.commentOwnerId;
  }

  public void setCommentOwnerName(String commentOwnerName){
    this.commentOwnerName = commentOwnerName;
  }
  public String getCommentOwnerName(){
    return this.commentOwnerName;
  }

  public void setFatherCommentId(Long fatherCommentId){
    this.fatherCommentId = fatherCommentId;
  }
  public Long getFatherCommentId(){
    return this.fatherCommentId;
  }

  public void setContent(String content){
    this.content = content;
  }
  public String getContent(){
    return this.content;
  }

  public String getCommentOwnerAvatar(){ return this.commentOwnerAvatar; }
  public void setCommentOwnerAvatar(String commentOwnerAvatar){ this.commentOwnerAvatar = commentOwnerAvatar; }
  
}
