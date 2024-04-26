package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

import java.util.Date;


@Entity
@Table(name = "ITEM")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemname;

    @Column(nullable = false)
    private String itemIntroduction;

    @Column(nullable = true)
    private Date creationDate;

    @Column(nullable = false)
    private double score = 0.0;

    @Column(nullable = false)
    private double totalScore = 0.0;

    @Column(nullable = false)
    private int scoreCount = 0;

    @Column(nullable = false)
    private int likes = 0;

    @Column(nullable = false)
    private Long itemTopicId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;  // 关联的Topic

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemIntroduction() {
        return itemIntroduction;
    }

    public void setItemIntroduction(String itemIntroduction) {
        this.itemIntroduction = itemIntroduction;
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

    public void setScore(double score) {
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

    public Long getItemTopicId() { return itemTopicId; }

    public void setItemTopicId(Long itemTopicId) { this.itemTopicId = itemTopicId; }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
