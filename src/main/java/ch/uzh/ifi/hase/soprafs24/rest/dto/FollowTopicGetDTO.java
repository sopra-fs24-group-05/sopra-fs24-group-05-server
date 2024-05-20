package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class FollowTopicGetDTO {
  private Long followTopicId;
  private String followTopicname;

  public void setFollowTopicId(Long followTopicId){
    this.followTopicId = followTopicId;
  }
  public Long getFollowTopicId(){
    return this.followTopicId;
  }

  public void setFollowTopicname(String followTopicname){
    this.followTopicname = followTopicname;
  }
  public String getFollowTopicname(){
    return this.followTopicname;
  }
  
}
