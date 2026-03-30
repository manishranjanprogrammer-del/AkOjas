package com.ojassoft.astrosage.varta.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.KundliChatHistoryBean;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class CDatabaseHelper extends SQLiteOpenHelper {
    /**
     * This field is used to create data base,so the database name is assigned
     * in this field ,used in this application.
     */
    public static final String DATABASE_NAME = "OjassoftMKDb";

    /**
     * This is a context variable used in this class.
     */
    protected Context context;

    /**
     * This is the constructor of this class to initialize variables,used in the
     * whole class
     */
    /**
     * This constructor is added by Bijendra on 18-Dec-2012
     *
     * @param context
     */
    public CDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 12 /* in app version 3.0 */); // DB
        // version
        // 5 in
        // app
        // version
        // 2.99
        this.context = context; // db version increse by 1 before 7 now 8 by
        // Shelendra on 28 .05 .2015 because off
        // timezone fix for LEICESTER city in db.
    }

    /**
     * This function is called with different parameters to create table with
     * data in database for this application.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createOrInsertData(db, R.raw.sqlcountry);
        createOrInsertData(db, R.raw.sqltimezone);
        createOrInsertData(db, R.raw.sqlcity);
        createOrInsertData(db, R.raw.sqlpersonalinfomisc);
        createOrInsertData(db, R.raw.sqlchartinfo);
        createOrInsertData(db, R.raw.sqlpersonalinfo);
        createOrInsertData(db, R.raw.sqlchathistory);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if ((oldVersion != newVersion) && (newVersion == 7)) {
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_TimeStamp long not null default 0");
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD onlinechartid VARCHAR(30)");
            Cursor cursorOnlineIds = db
                    .rawQuery(
                            "SELECT localchartid, onlinechartid FROM tblLocalOnlineChartRelation",
                            null);
            String[][] onlineLocalRelation = null;
            if (cursorOnlineIds != null) {
                if (cursorOnlineIds.getCount() > 0) {
                    onlineLocalRelation = new String[cursorOnlineIds.getCount()][2];
                    if (cursorOnlineIds.moveToFirst()) {
                        int i = 0;
                        do {
                            onlineLocalRelation[i][0] = cursorOnlineIds
                                    .getString(0);
                            onlineLocalRelation[i][1] = cursorOnlineIds
                                    .getString(1);
                            i++;
                        } while (cursorOnlineIds.moveToNext());
                        for (int j = 0; j < onlineLocalRelation.length; j++) {
                            ContentValues args = new ContentValues();
                            args.put("onlinechartid", onlineLocalRelation[j][1]);
                            db.update("tblHoroPersonalInfo", args, "id" + "="
                                    + onlineLocalRelation[j][0], null);
                        }
                    }
                }
            }
        }
        if ((oldVersion != newVersion) && (newVersion == 9)) {
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_State VARCHAR(50) DEFAULT 'not define'");
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_Country VARCHAR(50) DEFAULT 'not define'");
        }

        if(newVersion == 12){
            db.execSQL("DROP TABLE IF EXISTS tblKundaliAiChatHistory");
            createOrInsertData(db, R.raw.sqlchathistory);
        }
    }

    /**
     * This is a main function called by other function to create single table
     * based on Resource id of xml file in RAW folder in the project
     *
     * @param dbIn
     * @param intResourceID
     * @return void
     */
    private void createOrInsertData(SQLiteDatabase dbIn, int intResourceID) {
        String s;
        try {
            InputStream in = context.getResources().openRawResource(
                    intResourceID);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(in, null);
            NodeList statements = doc.getElementsByTagName("statement");
            for (int i = 0; i < statements.getLength(); i++) {
                s = statements.item(i).getChildNodes().item(0).getNodeValue();
                dbIn.execSQL(s);
            }
        } catch (Throwable t) {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.close();
        } catch (Exception e) {

        }
        super.finalize();
    }


    public void insertQuestion(long id, String conversationId, String question, String time){
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()){
            ContentValues values = new ContentValues();
            values.put("id",id);
            values.put("conversation_id",conversationId);
            values.put("question_text",question);
            values.put("time_stamp",time);
            sqLiteDatabase.insert("tblKundaliChatMessage", null, values);
        }catch(Exception e){
        }
    }

    public void updateAnswer(String  conversationId,long rowId,String answerId,String question,String answer,int isLiked,int isDisliked,String time){
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()){
            ContentValues values = new ContentValues();
            values.put("answer_id",answerId);
            values.put("answer_text",answer);
            values.put("is_liked",isLiked);
            values.put("is_disliked",isDisliked);
            values.put("time_stamp",time);

            int updated =  sqLiteDatabase.update("tblKundaliChatMessage", values, "id" + " = ?",
                        new String[]{String.valueOf(rowId)});
                //Log.e("TestChatHistory", "saveAnswerToDb() >> isAnswerUpdated = " + updated);

            values.put("conversation_id",conversationId);
            values.put("question_text",question);
            if(updated <= 0) {
                long id = sqLiteDatabase.insert("tblKundaliChatMessage", null, values);
                //Log.e("TestChatHistory", "saveAnswerToDb() >> inserted row Id = " + id);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateConversation(String conversation,String question,String answer,String dateAndTime){
        ContentValues values = new ContentValues();
        values.put("title_text",question);
        values.put("description_text",answer);
        values.put("time_stamp",dateAndTime);
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()) {
            sqLiteDatabase.update("tblKundaliAiChatHistory", values, "conversation_id" + " = ?",
                    new String[]{String.valueOf(conversation)});
        }catch(Exception ignore){
        }
    }

    private String addConversation(String localChartId,String onlineChartId,String userId,String moduleName){
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()){
            ContentValues values = new ContentValues();
            if(TextUtils.isEmpty(onlineChartId) || onlineChartId.equals("-1")){
                onlineChartId = String.valueOf(System.currentTimeMillis());
            }
            if(TextUtils.isEmpty(localChartId) || localChartId.equals("-1")){
                localChartId = String.valueOf(System.currentTimeMillis());
            }
           values.put("conversation_id",onlineChartId);
           values.put("astrologer_id","38");
            values.put("title_text","");
            values.put("description_text","");
            values.put("local_chart_id",localChartId);
            values.put("online_chart_id",onlineChartId);
            values.put("user_id",userId);
            values.put("module_name",moduleName);
            sqLiteDatabase.insert("tblKundaliAiChatHistory", null, values);
            return onlineChartId;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private String addConversation(String userId,String conversationDetails,String moduleName){
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()){
            ContentValues values = new ContentValues();

            String localChartId = String.valueOf(System.currentTimeMillis());

            values.put("conversation_id",conversationDetails);
            values.put("astrologer_id","38");
            values.put("title_text","");
            values.put("description_text","");
            values.put("local_chart_id",localChartId);
            values.put("online_chart_id",localChartId);
            values.put("user_id",userId);
            values.put("module_name",moduleName);
            sqLiteDatabase.insert("tblKundaliAiChatHistory", null, values);
            return conversationDetails;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String getConversationId(String localChatId,String onlineChartId,String userId,String moduleName){
        String selectQuery = "SELECT conversation_id,module_name FROM tblKundaliAiChatHistory WHERE (local_chart_id = ? OR online_chart_id = ?) AND user_id = ?";
        String conversationId = "";
        try (SQLiteDatabase db = this.getReadableDatabase()){
             Cursor cursor = db.rawQuery(selectQuery,new String[]{String.valueOf(localChatId),String.valueOf(onlineChartId),String.valueOf(userId)});
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("conversation_id");
                if (columnIndex != -1) {
                    String localId = cursor.getString(columnIndex);
                    String screenName = cursor.getString(cursor.getColumnIndex("module_name"));
                    if(TextUtils.isEmpty(screenName)){
                        setConversationName(localId,moduleName);
                    }
                    if (localId != null) {
                        conversationId = localId;
                    }
                }
             }else{
                 conversationId = addConversation(localChatId,onlineChartId,userId,moduleName);
             }
             cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return conversationId;
    }

    public String getConversationId(String userId,String conversationDetails,String moduleName){
        String selectQuery = "SELECT conversation_id,module_name FROM tblKundaliAiChatHistory WHERE conversation_id = ? AND user_id = ?";
        String conversationId = "";
        try (SQLiteDatabase db = this.getReadableDatabase()){
            Cursor cursor = db.rawQuery(selectQuery,new String[]{conversationDetails,userId});
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("conversation_id");
                if (columnIndex != -1) {
                    String localId = cursor.getString(columnIndex);
                    String screenName = cursor.getString(cursor.getColumnIndex("module_name"));
                    if(TextUtils.isEmpty(screenName)){
                        setConversationName(localId,moduleName);
                    }
                    if (localId != null) {
                        conversationId = localId;
                    }
                }
            }else {
                conversationId = addConversation(userId,conversationDetails,moduleName);
            }
            cursor.close();
        }catch(Exception ignore){}
        return conversationId;
    }

    public ArrayList<KundliChatHistoryBean> getKundaliChatHistory(String userId,int startIndex,int limit){
        ArrayList<KundliChatHistoryBean> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM  tblKundaliAiChatHistory WHERE user_id = ? ORDER BY time_stamp DESC LIMIT ?,?";
        try (SQLiteDatabase db = this.getReadableDatabase();Cursor cursor = db.rawQuery(selectQuery,new String[]{userId,String.valueOf(startIndex),String.valueOf(limit)})){

            if(cursor.moveToFirst()){
                do{
                    String title = cursor.getString(cursor.getColumnIndex("title_text"));
                    String conversation = cursor.getString(cursor.getColumnIndex("conversation_id"));
                    if(getChatHistoryCount(conversation) < 1 ) continue;
                    if(TextUtils.isEmpty(title)) continue;
                    KundliChatHistoryBean bean = new KundliChatHistoryBean();
                    bean.setAstrologerId(cursor.getString(cursor.getColumnIndex("astrologer_id")));
                    bean.setConversationId(conversation);
                    bean.setTitle(title);
                    bean.setDescription(cursor.getString(cursor.getColumnIndex("description_text")));
                    bean.setLocalChartId(cursor.getString(cursor.getColumnIndex("local_chart_id")));
                    bean.setOnlineChartId(cursor.getString(cursor.getColumnIndex("online_chart_id")));
                    bean.setConversationEndTime(cursor.getString(cursor.getColumnIndex("time_stamp")));
                    bean.setModuleName(cursor.getString(cursor.getColumnIndex("module_name")));
                    list.add(bean);
                }while(cursor.moveToNext());
            }

        }

        return list;
    }

    public ArrayList<ChatMessage> getConversation(String conversationId,int startIndex, int limit){
        String query = "SELECT * FROM (SELECT * FROM tblKundaliChatMessage WHERE conversation_id = ? ORDER BY time_stamp DESC LIMIT ?,?) sub ORDER BY time_stamp ASC";
        ArrayList<ChatMessage> list = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query,new String[]{conversationId,String.valueOf(startIndex),String.valueOf(limit)})) {
            if(cursor.moveToFirst()) {
                do {
                    String userText = cursor.getString(cursor.getColumnIndex("question_text"));
                    String astroText = cursor.getString(cursor.getColumnIndex("answer_text"));
                    String answerId = cursor.getString(cursor.getColumnIndex("answer_id"));
                    long ansId ;
                    try{
                        ansId = Long.parseLong(answerId);
                    }catch(NumberFormatException e){
                        ansId = System.currentTimeMillis();
                    }
                    if(userText !=null && !userText.isEmpty()){
                        Message userMessage = new Message();
                        userMessage.setChatId(ansId);
                        userMessage.setMessageBody(userText);
                        userMessage.setAuthor("USER");
                        userMessage.setDateCreated(cursor.getString(cursor.getColumnIndex("time_stamp")));
                        userMessage.setLike(cursor.getInt(cursor.getColumnIndex("is_liked")));
                        userMessage.setUnlike(cursor.getInt(cursor.getColumnIndex("is_disliked")));
                        list.add(new UserMessage(userMessage));
                    }
                    if(astroText !=null && !astroText.isEmpty()){
                        Message astroMessage = new Message();
                        astroMessage.setChatId(ansId);
                        astroMessage.setMessageBody(astroText);
                        astroMessage.setAuthor("Astrologer");
                        astroMessage.setDateCreated(cursor.getString(cursor.getColumnIndex("time_stamp")));
                        astroMessage.setLike(cursor.getInt(cursor.getColumnIndex("is_liked")));
                        astroMessage.setUnlike(cursor.getInt(cursor.getColumnIndex("is_disliked")));
                        list.add(new UserMessage(astroMessage));
                    }
                } while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public int getChatHistoryCount(String conversationId){
        int count = 0;
        String query = "SELECT question_text,answer_text FROM tblKundaliChatMessage WHERE conversation_id = ?";
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query,new String[]{conversationId})){
            if(cursor.moveToFirst()){
                do{
                    String userText = cursor.getString(cursor.getColumnIndex("question_text"));
                    String astroText = cursor.getString(cursor.getColumnIndex("answer_text"));

                    if(TextUtils.isEmpty(userText) && TextUtils.isEmpty(astroText)) continue;

                    count++;
                }while(cursor.moveToNext());

            }
            //Log.e("TestChatHistory", "getIsHistoryAvailable() >> Column length = " + cursor.getCount());
        }catch(Exception ignore){}
        return count;
    }

    public void setLikeDislike(String answerId,int isLike,int isDisLike){

        ContentValues values = new ContentValues();
        values.put("is_liked",isLike);
        values.put("is_disliked",isDisLike);
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()) {

            sqLiteDatabase.update("tblKundaliChatMessage", values, "answer_id" + " = ?",
                    new String[]{String.valueOf(answerId)});

        }catch(Exception ignore){
        }
    }

    public void deleteUserMessage(ChatMessage message){
        ContentValues values = new ContentValues();
        if(message.getAuthor().equalsIgnoreCase("USER")){
            values.put("question_text","");
        }else{
            values.put("answer_text","");
        }
        try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()) {
            sqLiteDatabase.update("tblKundaliChatMessage", values, "answer_id" + " = ?",
                    new String[]{String.valueOf(message.chatId())});
        }catch(Exception ignore){
        }
    }

    public void setConversationName(String conversationId,String moduleName){

            ContentValues values = new ContentValues();
            values.put("module_name",moduleName);
            try(SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase()) {

                 sqLiteDatabase.update("tblKundaliAiChatHistory", values, "conversation_id  = ?",
                    new String[]{conversationId});

        }catch(Exception ignore){}
    }

}
