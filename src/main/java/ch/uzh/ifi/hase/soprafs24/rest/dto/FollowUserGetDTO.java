package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class FollowUserGetDTO {
  private Long followUserId;
  private String followUsername;

  public void setFollowUserId(Long followUserId){
    this.followUserId = followUserId;
  }
  public Long getFollowUserId(){
    return this.followUserId;
  }

  public void setFollowUsername(String followUsername){
    this.followUsername = followUsername;
  }
  public String getFollowUsername(){
    return this.followUsername;
  }
  
}
