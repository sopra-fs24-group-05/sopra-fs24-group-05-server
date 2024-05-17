package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class CommentStatusGetDTO {
  private boolean isAlreadyLiked;
  private int thumbsUpNum;

  public void setIsAlreadyLiked(boolean isAlreadyLiked){
    this.isAlreadyLiked=isAlreadyLiked;
  }

  public boolean getisAlreadyLiked(){
    return this.isAlreadyLiked;
  }

  public void setThumbsUpNum(int thumbsUpNum){
    this.thumbsUpNum=thumbsUpNum;
  }

  public int getThumbsUpNum(){
    return this.thumbsUpNum;
  }
  
}

