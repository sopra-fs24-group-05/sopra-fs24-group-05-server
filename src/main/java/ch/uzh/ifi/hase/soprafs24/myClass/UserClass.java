package ch.uzh.ifi.hase.soprafs24.myClass;

import java.util.Date;
import java.util.List;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

public class UserClass {
  private Long userId;
  private String password;
  private String username;
  private String token;
  private Date birthday;
  private UserStatus status;
  private boolean commentAllowed;
  private boolean displayAllowed;
  private List<Long> followingTopics;


  public UserClass(){
    
  }

  
}
