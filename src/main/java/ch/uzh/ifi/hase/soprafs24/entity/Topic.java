package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "TOPIC")
public class Topic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String TopicName;

    @Column(nullable = false)
    private Date CreationDate;

    @Column(nullable = false)
    private Long ownerId;

    @Column
    private Long fatherTopicId;

    @Column
    private Long sonTopicId;

    @Column(nullable = false)
    private Boolean editAllowed;

    @Column
    private String content;

    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}

    public String getTopicName() {return TopicName;}

    public void setTopicName(String topicName) {TopicName = topicName;}

    public Date getCreationDate() {return CreationDate;}

    public void setCreationDate(Date creationDate) {CreationDate = creationDate;}

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
