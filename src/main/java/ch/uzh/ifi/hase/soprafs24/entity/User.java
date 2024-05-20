package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserIdentity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */

/*
 * import javax.persistence.* to utlize keywords start with @
 */

/**
 * @Entity: Indicates that the class is an entity, 
 * which means it will be mapped to a database table.
 * @Table(name = "USER"): Specifies the name of the database table 
 * to which this entity is mapped.
 * @Id: Specifies the primary key of the entity.
 * @GeneratedValue: Specifies that the value of the primary key will be generated automatically by the database.
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  public static Long getId;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  /*
  @Column(nullable = true)
  private String name;
  */

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus status;

  @Column(nullable = false)
  private UserIdentity identity;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true)
  private Date createDate;

  @Column(nullable = true)
  private Date birthday;

  /**
   * store a string point to the avatar
   */
  @Column(nullable = false, columnDefinition = "TEXT")
  private String avatar;

  @ElementCollection
  @CollectionTable(name = "follow_Item_List", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "follow_item_id", nullable = false)
  private List<Long> followItemList = new ArrayList<Long>();

  @ElementCollection
  @CollectionTable(name = "follow_User_List", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "follow_user_id", nullable = false)
  private List<Long> followUserList = new ArrayList<Long>();

  @ElementCollection
  @CollectionTable(name = "follow_Topic_List", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "follow_topic_id", nullable = false)
  private List<Long> followTopicList = new ArrayList<Long>();

  //@Column(columnDefinition = "TEXT", nullable = true)
  //private String followTopicList;

  public Long getUserId() {
    return userId;
  }

  /* 
   * Only to be used in test
  */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAvater(){
        return this.avatar;
    }

    public void setAvater(String avatar){
        this.avatar=avatar;
    }

    public void setIdentity(UserIdentity identity) { this.identity = identity; }
    public UserIdentity getIdentity() { return identity; }

    public List<Long> getFollowItemList() {
        return this.followItemList;
    }
    public void setFollowItemList(List<Long> followItemList) {
        this.followItemList = followItemList;
    }

    public List<Long> getFollowUserList(){
        return this.followUserList;
    }
    public void setFollowUserList(List<Long> followUserList){
        this.followUserList = followUserList;
    }

    public void setFollowTopicList(List<Long> followTopicList){
        this.followTopicList = followTopicList;
    }
    public List<Long> getFollowTopicList(){
        return this.followTopicList;
    }
}
