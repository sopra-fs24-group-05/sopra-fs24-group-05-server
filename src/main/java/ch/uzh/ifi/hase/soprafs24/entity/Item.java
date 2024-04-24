package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

import java.util.Date;


@Entity
@Table(name = "ITEM")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false)
    private double score = 0.0;

    @Column(nullable = false)
    private double totalScore = 0.0;

    @Column(nullable = false)
    private int scoreCount = 0;

    @Column(nullable = false)
    private int likes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topicName", nullable = false)
    private Topic topic;  // 关联的Topic

    // Getters and Setters
    public Long getId() {
        return itemId;
    }

    public void setId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public double getScore() {
        return score;
    }

    public void setAverageScore(double score) {
        this.score = score;
    }

    public void addLike() {
        this.likes++;
    }

    public void addScore(double score) {
        this.totalScore += score;
        this.scoreCount++;
    }

    public double getAverageScore() {
        if (scoreCount > 0) {
            return totalScore / scoreCount;
        }
        return 0.0;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
