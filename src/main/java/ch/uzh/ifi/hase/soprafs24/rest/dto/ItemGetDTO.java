package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;

import javax.persistence.Column;
import java.util.Date;

public class ItemGetDTO {
    private Long itemId;

    private String itemName;

    private String content;

    private Date creationDate;

    private double averageScore = 0.0;

    private int likes = 0;

    private Topic topic;

    private Integer topicId;



    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {return itemName;}

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {this.likes = likes;}

    public double getAverageScore() {return averageScore;}

    public void setAverageScore(double averageScore) {this.averageScore = averageScore;}

    public void addLike() {
        this.likes++;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Integer getTopicId() { return topicId; }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

}
