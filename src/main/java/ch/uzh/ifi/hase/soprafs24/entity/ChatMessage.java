package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

@Entity
@Table(name = "ChatMessage")
public class ChatMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long messageId;

  @Column(nullable = true, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private Long itemId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String userName;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String userAvatar;

  @Column
  private String messageTime;

  // getter and setter
  public Long getMessageId(){ return this.messageId; }
  public void setMessageId(Long messageId){ this.messageId = messageId; }

  public String getContent(){ return this.content; }
  public void setContent(String content){ this.content = content; }

  public Long getItemId(){ return this.itemId; }
  public void setItemId(Long itemId){ this.itemId = itemId; }

  public Long getUserId(){ return this.userId; }
  public void setUserId(Long userId){ this.userId = userId; }

  public String getUserName(){ return this.userName; }
  public void setUserName(String userName){ this.userName = userName; }

  public String getUserAvatar(){ return this.userAvatar; }
  public void setUserAvatar(String userAvatar){ this.userAvatar = userAvatar; }

  public String getMessageTime(){ return this.messageTime; }
  public void setMessageTime(String messageTime){ this.messageTime = messageTime; }
}
