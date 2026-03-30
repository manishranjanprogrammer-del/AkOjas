package com.ojassoft.astrosage.varta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.MessageAdapter;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.Collections;

public class ChatMessageDB extends SQLiteOpenHelper {

    static final String NAME = "varta_chat.db";
    static final int VERSION = 1;
    static final String TABLE_NAME = "tbl_chat_message";
    static final String COLUMN_MSG_ID = "msg_id";
    static final String COLUMN_AUTHOR = "author";
    static final String COLUMN_MSG_BODY = "msg_body";
    static final String COLUMN_IS_SEEN = "is_seen";
    static final String COLUMN_USER_ID = "user_id";
    static final String COLUMN_ASTROLOGER_ID = "astrologer_id";
    static final String COLUMN_ASTROLOGER_TYPE = "astrologer_type";
    static final String COLUMN_CHANNEL_ID = "channel_id";
    static final String COLUMN_IS_LIKED = "is_liked";
    static final String COLUMN_IS_DISLIKED = "is_disliked";
    static final String COLUMN_TIME_STAMP = "time_stamp";

    static final String CREATE_MSG_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +"("+
                                                    COLUMN_MSG_ID + " INT,"+
                                                    COLUMN_AUTHOR + " VARCHAR(10),"+
                                                    COLUMN_MSG_BODY+" TEXT,"+
                                                    COLUMN_IS_SEEN +" TINYINT(1),"+
                                                    COLUMN_USER_ID +" VARCHAR(100),"+
                                                    COLUMN_ASTROLOGER_ID+" VARCHAR(100),"+
                                                    COLUMN_ASTROLOGER_TYPE+" TINYINT(1),"+
                                                    COLUMN_CHANNEL_ID +"  TEXT,"+
                                                    COLUMN_IS_LIKED+" TINYINT(1),"+
                                                    COLUMN_IS_DISLIKED+"  TINYINT(1),"+
                                                    COLUMN_TIME_STAMP+" INT );";

    public ChatMessageDB(Context context){
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("SQL_Query", CREATE_MSG_TABLE_QUERY);
        db.execSQL(CREATE_MSG_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addMessage(UserMessage msg,String channelID,String astrologerId,String userId){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MSG_ID, msg.chatId());
        values.put(COLUMN_AUTHOR, msg.getAuthor());
        values.put(COLUMN_MSG_BODY, msg.getMessageBody());
        values.put(COLUMN_IS_SEEN, msg.isSeen() ? 0 : 1);
        values.put(COLUMN_IS_LIKED, false);
        values.put(COLUMN_IS_DISLIKED, false);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_ASTROLOGER_ID, astrologerId);
        values.put(COLUMN_CHANNEL_ID, channelID);
        values.put(COLUMN_TIME_STAMP, msg.getTimeStamp());
        //Log.d("ChatHistoryDAO","message = " + msg);
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            // Check if a record with the same timestamp exists
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_TIME_STAMP + " = ?";
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(msg.getTimeStamp())});

            boolean exists = false;
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
            cursor.close();

            // If no duplicate found, insert the new record
            if (!exists) {
                sqLiteDatabase.insert(TABLE_NAME, null, values);
            }
        } catch (Exception ignore) {}
    }

    public int getChatHistoryCount(String astrologerId){
        int count = 0;
        String query = "SELECT "+COLUMN_MSG_BODY+" FROM "+ TABLE_NAME +" WHERE " + COLUMN_ASTROLOGER_ID +" = ?;";
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query,new String[]{astrologerId})){
            if(cursor.moveToFirst()){
                count = cursor.getCount();
            }
            //Log.e("TestChatHistory", "getIsHistoryAvailable() >> Column length = " + cursor.getCount());
        }catch(Exception ignore){}
        return count;
    }

    public void setLikeDislike(long msgId,int isLike,int isDisLike){

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_LIKED,isLike);
        values.put(COLUMN_IS_DISLIKED,isDisLike);
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()) {

            sqLiteDatabase.update(TABLE_NAME, values, COLUMN_MSG_ID + " = ?",
                    new String[]{String.valueOf(msgId)});

        }catch(Exception ignore){
        }
    }


    public ArrayList<ChatMessage> getOldMessages(String userID,String astrologerId,boolean isAiAsto, int startIndex, int limit){
                String query = "SELECT * FROM " + TABLE_NAME +" WHERE "+COLUMN_ASTROLOGER_ID+" = ? AND "+COLUMN_USER_ID + " = ? ORDER BY "+COLUMN_TIME_STAMP +" DESC LIMIT ?,?";
        ArrayList<ChatMessage> list = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query,new String[]{astrologerId,userID,String.valueOf(startIndex),String.valueOf(limit)})) {
            if(cursor.moveToFirst()) {
                do {
                    UserMessage userMessage = new UserMessage();
                    userMessage.setChatId(cursor.getLong(cursor.getColumnIndex(COLUMN_MSG_ID)));
                    userMessage.setMessageBody(cursor.getString(cursor.getColumnIndex(COLUMN_MSG_BODY)));
                    userMessage.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                    userMessage.setDateCreated(MessageAdapter.convertDate(cursor.getLong(cursor.getColumnIndex(COLUMN_TIME_STAMP))));
                    userMessage.setTimeStamp(cursor.getLong(cursor.getColumnIndex(COLUMN_TIME_STAMP)));
                    userMessage.setLike(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_LIKED)));
                    userMessage.setUnlike(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DISLIKED)));
                    int isSeen = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SEEN));
                    if (!isAiAsto){
                        userMessage.setSeen(isSeen > 0);
                    }else {
                        userMessage.setSeen(false); // AI astrologer messages are not marked as seen
                    }
                    list.add(userMessage);

                } while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Collections.reverse(list);
        return list;
    }

    public void updateMessageStatus(UserMessage message,String astrologerId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IS_SEEN,message.isSeen() ? 1 : 0);
        contentValues.put(COLUMN_TIME_STAMP,message.getTimeStamp());
        try(SQLiteDatabase db = getWritableDatabase()){
             db.update(TABLE_NAME,contentValues,COLUMN_MSG_ID+" = ? AND " + COLUMN_ASTROLOGER_ID + " = ?",new String[]{String.valueOf(message.chatId()),astrologerId});
        }catch(Exception ignored){
        }
    }

}
