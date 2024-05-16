package ch.uzh.ifi.hase.soprafs24.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "TOPIC")
public class Topic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column Integer topicId;

    @Column(nullable = false, unique = true)
    private String topicName;

    @Column
    private Date creationDate;

    @Column
    private Integer ownerId;

    @Column
    private Long fatherTopicId;

    @Column
    private Long sonTopicId;

    @Column(nullable = false)
    private Boolean editAllowed;

    @Column
    private String description;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Item> items;

    @Column(name = "search_count")
    private int searchCount; // New field for search count

    @ElementCollection
    @CollectionTable(name = "topic_chatpools", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "message")
    private List<String> chatPool = new ArrayList<>();

    public void setTopicId(Integer topicId) {this.topicId = topicId;}

    public Integer getTopicId() {return topicId;}

    public String getTopicName() {return topicName;}

    public void setTopicName(String topicName) {this.topicName = topicName;}

    public Date getCreationDate() {return creationDate;}

    public void setCreationDate(Date creation_date) {this.creationDate = creation_date;}

    public Integer getOwnerId() {return ownerId;}

    public void setOwnerId(Integer ownerId) {this.ownerId = ownerId;}

    public Long getFatherTopicId() {return fatherTopicId;}

    public void setFatherTopicId(Long fatherTopicId) {this.fatherTopicId = fatherTopicId;}

    public Long getSonTopicId() {return sonTopicId;}

    public void setSonTopicId(Long sonTopicId) {this.sonTopicId = sonTopicId;}

    public Boolean getEditAllowed() {return editAllowed;}

    public void setEditAllowed(Boolean editAllowed) {this.editAllowed = editAllowed;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public List<String> getChatPool() { return chatPool; }
    public void setChatPool(List<String> chatPool) { this.chatPool = chatPool; }

    public int getSearchCount() {return searchCount;}

    public void setSearchCount(int searchCount) {this.searchCount = searchCount;}

}
