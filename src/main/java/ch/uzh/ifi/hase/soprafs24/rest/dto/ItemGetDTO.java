package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ItemGetDTO {
    private Long itemId;
    private Long userId;
    private Long itemTopicId;
    private String itemname;
    private String itemIntroduction;
    //private Date creationDate;
    private double score = 0.0;
    private double totalScore = 0.0;
    private int scoreCount = 0;
    private int likes = 0;

    public Long getItemId() {return itemId;}

    public void setItemId(Long itemId) {this.itemId = itemId;}

    public Long getUserId() {return userId;}

    public void setUserId(Long userId) {this.userId = userId;}

    public Long getItemTopicId() {return itemTopicId;}

    public void setItemTopicId(Long itemTopicId) {this.itemTopicId = itemTopicId;}

    public String getItemname() {return itemname;}

    public void setItemname(String itemname) {this.itemname = itemname;}

    public String getItemIntroduction() {
        return itemIntroduction;
    }

    public void setItemIntroduction(String itemIntroduction) {
        this.itemIntroduction = itemIntroduction;
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
}
