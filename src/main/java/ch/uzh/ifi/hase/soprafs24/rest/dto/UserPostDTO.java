package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPostDTO {

  private Long id;

  private String name;

  private String username;

  private String password;

  public String getName() {
    return name;
  }

  public Long getId(){
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
  }
}
