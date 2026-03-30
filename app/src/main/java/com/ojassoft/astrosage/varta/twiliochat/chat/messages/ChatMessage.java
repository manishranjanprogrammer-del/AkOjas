package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;

public interface ChatMessage {

  String getMessageBody(Context context);

  String getAuthor();

  String getDateCreated();

  boolean isSeen();
  void setSeen(boolean seen);

  boolean isDelayed();
  void  setDelayed(boolean isDelayed);
  long chatId();

  int getLike();
  int getUnlike();
  void setLike(int like);
  void setUnlike(int unlike);

  boolean getIsError();
  void setIsError(boolean isError);
  boolean getIsSpeaking();
  void setIsSpeaking(boolean isSpeaking);
  void setMessageBody(String body);
  String getAnswerId();

  int[] getPlanetsInRashiLagna() ;
  void setPlanetsInRashiLagna(int[] planetsInRashiLagna);
  double[] getPlanetDegreeArray();
  void setPlanetDegreeArray(double[] planetDegreeArray);
  boolean isAnswerIdMissing();
  long getTimeStamp();
  void setTimeStamp(long timeStamp);
}
