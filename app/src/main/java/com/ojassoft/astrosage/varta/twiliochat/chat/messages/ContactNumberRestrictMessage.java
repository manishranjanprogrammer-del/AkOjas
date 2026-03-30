package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;

import com.ojassoft.astrosage.R;

public class ContactNumberRestrictMessage extends StatusMessage {

  public ContactNumberRestrictMessage(String author) {
    super(author);
  }

  @Override
  public String getMessageBody(Context context) {
    return context.getResources().getString(R.string.legal_consequences);
  }

    @Override
    public void setMessageBody(String body) {

    }
}
