package com.ojassoft.astrosage.varta.dao;

import android.content.Context;
import android.util.Log;

import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryDAO {
    ChatMessageDB dbHelper;
    String userId;
    private static ChatHistoryDAO INSTANCE;

    private ChatHistoryDAO(Context context){
        this.dbHelper = new ChatMessageDB(context);
        userId = CUtils.getUserID(context);
    }

    public int getChatHistoryCount(String astrologerId){
        return dbHelper.getChatHistoryCount(astrologerId);
    }

    public void setLikeDislike(long msgId,int isLike,int isDislike){
        dbHelper.setLikeDislike(msgId,isLike,isDislike);
    }
    public ArrayList<ChatMessage> getOldMessages(String astrologerId,boolean isAiAstro, int index, int limit){
        Log.d("ChatHistoryDAO", "getOldMessages => astrologerId: " + astrologerId + ", index: " + index + ", limit: " + limit);
        return dbHelper.getOldMessages(userId,astrologerId,isAiAstro,index,limit);
    }

    public ArrayList<ChatMessage> getOldMessages(String astrologerId, int index, int limit){
        //Log.d("ChatHistoryDAO", "getOldMessages => astrologerId: " + astrologerId + ", index: " + index + ", limit: " + limit);
        return dbHelper.getOldMessages(userId,astrologerId,false,index,limit);
    }
    public static ChatHistoryDAO getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new ChatHistoryDAO(context);
        }
        return INSTANCE;
    }

    public void addMessage(UserMessage msg,String ChannelID,String astrologerID){
        dbHelper.addMessage(msg,ChannelID,astrologerID,userId);
    }
    public void updateMessageStatus(UserMessage message,String astrologerId){
         dbHelper.updateMessageStatus(message,astrologerId);
    }
    @Override
    protected void finalize(){
        try {
            dbHelper.close();
        } catch (Exception ignored) {

        }
    }
}
