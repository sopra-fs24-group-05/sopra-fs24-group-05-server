package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "TOPIC")
public class Topic implements Serializable {
    private static final long serialVersionUID = 0L;

    @Id
    @GeneratedValue
    private Long topicId;

    @Column(nullable = false)
    private String topicName;

    @Column(nullable = false)
    private Date creation_date;

    @Column(nullable = true)
    private Long ownerId;

    @Column
    private Long fatherTopicId;

    @Column
    private Long sonTopicId;

    @Column(nullable = false)
    private Boolean editAllowed;

    @Column
    private String topicIntroduction;

//    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Item> items;


    @ElementCollection
    @CollectionTable(name = "topic_chatpools", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "message")
    private List<String> chatPool = new ArrayList<>();

    public void setTopicId(Long topicId) {this.topicId = topicId;}

    public Long getTopicId() {return topicId;}

    public String getTopicName() {return topicName;}

    public void setTopicName(String topicName) {this.topicName = topicName;}

    public Date getCreationDate() {return creation_date;}

    public void setCreationDate(Date creation_date) {this.creation_date = creation_date;}

    public Long getOwnerId() {return ownerId;}

    public void setOwnerId(Long ownerId) {this.ownerId = ownerId;}

    public Long getFatherTopicId() {return fatherTopicId;}

    public void setFatherTopicId(Long fatherTopicId) {this.fatherTopicId = fatherTopicId;}

    public Long getSonTopicId() {return sonTopicId;}

    public void setSonTopicId(Long sonTopicId) {this.sonTopicId = sonTopicId;}

    public Boolean getEditAllowed() {return editAllowed;}

    public void setEditAllowed(Boolean editAllowed) {this.editAllowed = editAllowed;}

    public String getTopicIntroduction() {return topicIntroduction;}

    public void setTopicIntroduction(String topicIntroduction) {this.topicIntroduction = topicIntroduction;}

    public List<String> getChatPool() { return chatPool; }
    public void setChatPool(List<String> chatPool) { this.chatPool = chatPool; }

//    public List<Item> getItems() {
//        return items;
//    }
//
//    public void setItems(List<Item> items) {
//        this.items = items;
//    }
}
