package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Date;

public class TopicPostDTO {

    private int topicId;

    private String topicName;

    private int ownerId;

    private Date creationDate;

    private Long fatherTopicId;

    private Long sonTopicId;

    private Boolean editAllowed;

    private String description;


    // Getter 和 Setter 方法
    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Date getCreationDate(){return creationDate;}

    public void setCreationDate(Date creationDate) {this.creationDate = creationDate;}

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Long getFatherTopicId() {
        return fatherTopicId;
    }

    public void setFatherTopicId(Long fatherTopicId) {
        this.fatherTopicId = fatherTopicId;
    }

    public Long getSonTopicId() {
        return sonTopicId;
    }

    public void setSonTopicId(Long sonTopicId) {
        this.sonTopicId = sonTopicId;
    }

    public Boolean getEditAllowed() {
        return editAllowed;
    }

    public void setEditAllowed(Boolean editAllowed) {
        this.editAllowed = editAllowed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

