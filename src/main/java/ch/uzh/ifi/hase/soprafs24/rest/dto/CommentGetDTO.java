package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class CommentGetDTO {

  private Long commentId;
  private Long commentOwnerId;
  private String commentOwnerName;
  private Long commentItemId;
  private String commentItemName;
  private Long score;
  private String content;
  private Long thumbsUpNum;
  private String commentOwnerAvatar;
  private String commentItemTopicName;

  // Get & set methods
  public Long getCommentId(){ return this.commentId; }
  public void setCommentId(Long commentId){ this.commentId=commentId; }

  public Long getCommentOwnerId(){ return this.commentOwnerId; }
  public void setCommentOwnerId(Long commentOwnerId){ this.commentOwnerId = commentOwnerId; }

  public Long getCommentItemId(){ return this.commentItemId; }
  public void setCommentItemId(Long commentItemId){ this.commentItemId = commentItemId; }

  public String getCommentOwnerName() { return this.commentOwnerName; }
  public void setCommentOwnerName(String commentOwnerName) { this.commentOwnerName = commentOwnerName; }

  public Long getScore(){ return this.score; }
  public void setScore(Long score){ this.score=score; }

  public String getContent(){ return this.content; }
  public void setContent(String content){ this.content=content; }

  public Long getThumbsUpNum(){ return this.thumbsUpNum; }
  public void setThumbsUpNum(Long thumbsUpNum){ this.thumbsUpNum=thumbsUpNum; }

  public String getCommentOwnerAvatar(){ return this.commentOwnerAvatar; }
  public void setCommentOwnerAvatar(String commentOwnerAvatar){ this.commentOwnerAvatar = commentOwnerAvatar; }

  public String getCommentItemName(){ return this.commentItemName; }
  public void setCommentItemName(String commentItemName){ this.commentItemName = commentItemName; }

  public String getCommentItemTopicName(){ return this.commentItemTopicName; }
  public void setCommentItemTopicName(String commentItemTopicName){ this.commentItemTopicName = commentItemTopicName; }
  
}
