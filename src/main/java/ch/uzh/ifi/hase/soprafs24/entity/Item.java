package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import java.util.Date;


@Entity
@Table(name = "ITEM")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "id") private Long itemId;

    @Column(nullable = false)
  
    private String itemName;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private Date creationDate;

    @Column(nullable = false)
    private double score = 0.0;

    @Column(nullable = false)
    private int likes = 0;

    @Column(name = "topicId")
    private Integer topicId;

    @ManyToOne
    @JoinColumn(name = "topicId", referencedColumnName = "topicId", nullable = false, insertable = false, updatable = false)
    private Topic topic;

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

    public double getScore() {return score;}

    public void setScore(double score) {this.score = score;}

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

    public void updateScore(CommentRepository commentRepository) {
        Double averageScore = commentRepository.calculateAverageScoreByItemId(itemId);
        this.score = averageScore != null ? averageScore : 0.0;
    }
}
