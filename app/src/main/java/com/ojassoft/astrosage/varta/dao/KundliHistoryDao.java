package com.ojassoft.astrosage.varta.dao;

import android.content.Context;

import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.utils.CDatabaseHelper;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.ArrayList;

public class KundliHistoryDao {
    CDatabaseHelper dbHelper;
    long lastQuestionId = -1;
    int currentPage = 1;
    String conversationId;
    final String userId;
    private static volatile KundliHistoryDao instance;

    private KundliHistoryDao(Context context){

        dbHelper = new CDatabaseHelper(context);
        userId= CUtils.getCountryCode(context)+CUtils.getUserID(context);

    }

    public void saveQuestionToDb(String questionText,String dateAndTime){
        lastQuestionId  = System.currentTimeMillis();
        dbHelper.insertQuestion(lastQuestionId,conversationId,questionText,dateAndTime);
        //Log.e("TestChatHistory", "saveQuestionToDb() >> question : "  + questionText +" rowId= " + lastQuestionId);
    }


    public void saveAnswerToDb(String answerId,String  questionText,String answerText){
        //Log.e("TestChatHistory", "saveAnswerToDb() >> answerId : "  + answerId +" answer = " + answerText);
        String dateAndTime = CUtils.getDbNowDateAndTime();
        dbHelper.updateAnswer(conversationId,lastQuestionId,answerId,questionText,answerText,0,0,dateAndTime);

        dbHelper.updateConversation(conversationId,questionText,answerText,dateAndTime);
        lastQuestionId = -1;
    }

    public ArrayList<ChatMessage> getMessagePaged(String conversationId,int startIndex, int limit){
         ArrayList<ChatMessage> list = dbHelper.getConversation(conversationId,startIndex,limit);
        //Log.e("TestChatHistory", "getMessagePaged() >> list : "  + list +" \n startIndex = " + startIndex);
        if(list != null){
             return list;
         }
         return new ArrayList<>();
    }

    public static KundliHistoryDao getInstance(Context context){
        synchronized (KundliHistoryDao.class) {
            if (instance == null) {
                instance = new KundliHistoryDao(context.getApplicationContext());
            }
        }
        return instance;
    }

    public String getConversationId(String localChartId,String onlineChartId,String moduleName){
        lastQuestionId = -1;
        currentPage = 1;
       return conversationId = dbHelper.getConversationId(localChartId,onlineChartId,userId,moduleName);
    }

    public String getConversationId(String conversationDetails,String moduleName){
        lastQuestionId = -1;
        currentPage = 1;
        return conversationId = dbHelper.getConversationId(userId,conversationDetails,moduleName);
    }

    public int getChatHistoryCount(){
           return dbHelper.getChatHistoryCount(conversationId);
    }

    public void setLikeDislike(String  answerId,int isLike,int isDislike){
        dbHelper.setLikeDislike(answerId,isLike,isDislike);
    }

    public void deleteMessage(ChatMessage message){
        dbHelper.deleteUserMessage(message);
    }

    @Override
    protected void finalize(){
        try {
            dbHelper.close();
        } catch (Exception ignored) {

        }
    }
}


