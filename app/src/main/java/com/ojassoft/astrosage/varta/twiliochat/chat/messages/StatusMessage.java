package com.ojassoft.astrosage.varta.twiliochat.chat.messages;


import android.content.Context;

public class StatusMessage implements ChatMessage {
  private String author = "";

  public StatusMessage(String author) {
    this.author = author;
  }

  @Override
  public String getAuthor() {
    return author;
  }

  @Override
  public String getDateCreated() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isSeen()  {
    return false;
  }

  @Override
  public void setSeen(boolean seen) {

  }

  @Override
  public boolean isDelayed() {
    return false;
  }

  @Override
  public void setDelayed(boolean isDelayed) {

  }

  @Override
  public long chatId()   {
    return 0;
  }

  @Override
  public int getLike() {
    return 0;
  }

  @Override
  public int getUnlike() {
    return 0;
  }

  @Override
  public void setLike(int like) {}

  @Override
  public void setUnlike(int unlike) { }

  @Override
  public boolean getIsError() {
    return false;
  }

  @Override
  public void setIsError(boolean isError) {

  }

  @Override
  public boolean getIsSpeaking() {
    return false;
  }

  @Override
  public void setIsSpeaking(boolean isSpeaking) {

  }

  @Override
    public void setMessageBody(String body) {

    }

  @Override
  public String getAnswerId() {
    return "";
  }

  @Override
  public int[] getPlanetsInRashiLagna() {
    return null;
  }

  @Override
  public void setPlanetsInRashiLagna(int[] planetsInRashiLagna) {

  }

  @Override
  public double[] getPlanetDegreeArray() {
    return null;
  }

  @Override
  public void setPlanetDegreeArray(double[] planetDegreeArray) {

  }

  @Override
  public boolean isAnswerIdMissing() {
    return false;
  }

  @Override
  public long getTimeStamp() {
    return 0;
  }

  @Override
  public void setTimeStamp(long timeStamp) {

  }


  @Override
  public String getMessageBody(Context context) {
    throw new UnsupportedOperationException();
  }
}
