package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class FollowItemGetDTO {
  private Long followItemId;
  private String followItemname;
  private Long followItemTopicId;

  public void setFollowItemId(Long followItemId){ this.followItemId = followItemId; }
  public Long getFollowItemId(){ return this.followItemId; }

  public void setFollowItemname(String followItemname){ this.followItemname = followItemname; }
  public String getFollowItemname(){ return this.followItemname; }
  
  public void setFollowItemTopicId(Long followItemTopicId){ this.followItemTopicId = followItemTopicId;}
  public Long getFollowItemTopicId(){ return this.followItemTopicId; }
}
