package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Date;
public class TopicGetDTO {
    private Long id;

    private String topicName;

    private Date creation_date;

    private Long ownerId;

    private Long fatherTopicId;

    private Long sonTopicId;

    private Boolean editAllowed;

    private String content;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getTopicName() {return topicName;}

    public void setTopicName(String topicName) {this.topicName = topicName;}

    public Date getCreationDate() {return creation_date;}

    public void setCreationDate(Date CreationDate) {this.creation_date = CreationDate;}

    public Long getOwnerId() {return ownerId;}

    public void setOwnerId(Long ownerId) {this.ownerId = ownerId;}

    public Long getFatherTopicId() {return fatherTopicId;}

    public void setFatherTopicId(Long fatherTopicId) {this.fatherTopicId = fatherTopicId;}

    public Long getSonTopicId() {return sonTopicId;}

    public void setSonTopicId(Long sonTopicId) {this.sonTopicId = sonTopicId;}

    public Boolean getEditAllowed() {return editAllowed;}

    public void setEditAllowed(Boolean editAllowed) {this.editAllowed = editAllowed;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

}
