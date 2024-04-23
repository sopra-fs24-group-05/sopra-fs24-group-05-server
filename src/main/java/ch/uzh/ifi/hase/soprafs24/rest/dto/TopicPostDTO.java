package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Date;
public class TopicPostDTO {

    private String topicName;

    private Long ownerId;

    private Boolean editAllowed;

    private String content;

    public String getTopicName() {return topicName;}

    public void setTopicName(String topicName) {this.topicName = topicName;}

    public Long getOwnerId() {return ownerId;}

    public void setOwnerId(Long ownerId) {this.ownerId = ownerId;}

    public Boolean getEditAllowed() {return editAllowed;}

    public void setEditAllowed(Boolean editAllowed) {this.editAllowed = editAllowed;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

}
