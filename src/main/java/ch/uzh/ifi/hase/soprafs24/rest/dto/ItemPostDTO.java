package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ItemPostDTO {
  private Long itemId;
  private Long userId;
  private Long topicId;
  private String name;
  private String description;
  //private Date creationDate;
  private double score = 0.0;
  private double totalScore = 0.0;
  private int scoreCount = 0;
  private int likes = 0;

  public Long getId() {return itemId;}

  public void setId(Long itemId) {this.itemId = itemId;}

  public Long getUserId() {return userId;}

public void setUserId(Long userId) {this.userId = userId;}

public Long getTopicId() {return topicId;}

public void setTopicId(Long topicId) {this.topicId = topicId;}

  public String getName() {return name;}

  public void setName(String name) {this.name = name;}

  public String getDescription() {
      return description;
  }

  public void setDescription(String description) {
      this.description = description;
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
