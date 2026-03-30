package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;

public class LowBalanceMessage extends StatusMessage {


  public LowBalanceMessage(String author) {
    super(author);
  }

  @Override
  public String getMessageBody(Context context) {
    return "Your wallet balance is low and the chat will disconnect after 60 seconds.";
  }

    @Override
    public void setMessageBody(String body) {

    }
}
