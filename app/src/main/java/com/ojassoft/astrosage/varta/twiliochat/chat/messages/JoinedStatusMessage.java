package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;

public class JoinedStatusMessage extends StatusMessage {


  public JoinedStatusMessage(String author) {
    super(author);
  }

  @Override
  public String getMessageBody(Context context) {
    return this.getAuthor() + " joined the channel";
  }
}
