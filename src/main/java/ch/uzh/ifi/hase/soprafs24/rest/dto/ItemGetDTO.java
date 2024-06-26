package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Topic;

import javax.persistence.Column;
import java.util.Date;

public class ItemGetDTO {
    private Long itemId;
    private String itemName;
    private String content;
    private Date creationDate;
    private double averageScore;
    private int likes;
    private Topic topic;
    private Integer topicId;

    @Column
    private Integer popularity;

    public ItemGetDTO() {
        this.averageScore = 0.0;
        this.likes = 0;
        this.popularity = 0;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

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

    public double getAverageScore() {
        return Double.parseDouble(String.format("%.1f", averageScore));
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    // Additional Methods
    public void addLike() {
        this.likes++;
    }

    public void incrementPopularity() {
        this.popularity++;
    }
}
