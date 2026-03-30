package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;
import android.content.res.Resources;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

public class WelcomeStatusMessage extends StatusMessage {

  public WelcomeStatusMessage(String author) {
    super(author);
  }

  @Override
  public String getMessageBody(Context context) {
    return context.getResources().getString(R.string.varta_chat_intro);
  }
}
