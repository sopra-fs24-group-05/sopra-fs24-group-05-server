package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class MessagePostDTO {

  private Long messageId;
  private String content;
  private Long itemId;
  private Long userId;
  //private String userAvatar;
  //private String messageTime;

  // getter and setter
  public Long getMessageId(){ return this.messageId; }
  public void setMessageId(Long messageId){ this.messageId = messageId; }

  public String getContent(){ return this.content; }
  public void setContent(String content){ this.content = content; }

  public Long getItemId(){ return this.itemId; }
  public void setItemId(Long itemId){ this.itemId = itemId; }

  public Long getUserId(){ return this.userId; }
  public void setUserId(Long userId){ this.userId = userId; }

  //public String getUserAvatar(){ return this.userAvatar; }
  //public void setUserAvatar(String userAvatar){ this.userAvatar = userAvatar; }

  //public String getMessageTime(){ return this.messageTime; }
  //public void setMessageTime(String messageTime){ this.messageTime = messageTime; }
}
