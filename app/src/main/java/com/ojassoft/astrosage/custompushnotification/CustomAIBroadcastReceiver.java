package com.ojassoft.astrosage.custompushnotification;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.*;
import static com.ojassoft.astrosage.varta.utils.CUtils.chatWindowOpenType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;

public class CustomAIBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent resultIntent;
        if (chatWindowOpenType.equals("AI")) {
            chatWindowOpenType = "";
            resultIntent = new Intent(context, AIChatWindowActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.putExtra("newQuestion", intent.getStringExtra(KEY_AI_QUESTION));
        } else if (chatWindowOpenType.equals("human")) {
            chatWindowOpenType = "";
            resultIntent = new Intent(context, ChatWindowActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.putExtra(KEY_AI_QUESTION, intent.getStringExtra(KEY_AI_QUESTION));
            resultIntent.putExtra(KEY_REVERT_QUESTION_COUNT, intent.getStringExtra(KEY_REVERT_QUESTION_COUNT));
            resultIntent.putExtra(KEY_AI_ASTROLOGER_ID, intent.getStringExtra(KEY_AI_ASTROLOGER_ID));
        } else {
            resultIntent = new Intent(context, ActAppModule.class);
            resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(intent.getStringExtra("link")));
            resultIntent.putExtra(KEY_AI_QUESTION, intent.getStringExtra(KEY_AI_QUESTION));
            resultIntent.putExtra(KEY_REVERT_QUESTION_COUNT, intent.getStringExtra(KEY_REVERT_QUESTION_COUNT));
            resultIntent.putExtra(KEY_AI_ASTROLOGER_ID, intent.getStringExtra(KEY_AI_ASTROLOGER_ID));
        }
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(resultIntent);
    }
}
