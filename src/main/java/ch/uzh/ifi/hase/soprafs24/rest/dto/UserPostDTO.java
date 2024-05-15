package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPostDTO {

  private Long userId;

  private String username;

  private String password;

  private String token;

  public Long getuserId(){
    return userId;
  }

  public void setUserId(Long userId){
    this.userId=userId;
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

  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
  }
}
